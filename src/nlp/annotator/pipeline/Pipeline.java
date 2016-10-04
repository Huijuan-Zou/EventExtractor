package nlp.annotator.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.DependencyParseAnnotator;
import edu.stanford.nlp.pipeline.MorphaAnnotator;
import edu.stanford.nlp.pipeline.NERCombinerAnnotator;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.ParserAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.entity.EntityMention;
import nlp.annotator.util.entity.TimeEntity;
import nlp.annotator.util.entity.Value;
import nlp.corpus.document.ACEDocument;
import nlp.corpus.document.Document;
import nlp.util.Pair;
/**
 * @author Robin
 * @version 1.0
 * @since	April 23 2016
 */
public class Pipeline {
	
	private AnnotationPipeline pipeline;
	private final Logger log = Logger.getLogger(Pipeline.class.getName());
	public Pipeline() throws ClassNotFoundException, IOException {
		log.info("Pipeline Initializing.");
		pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(new TokenizerAnnotator(false, "en"));
		pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
		pipeline.addAnnotator(new POSTaggerAnnotator(false));
		pipeline.addAnnotator(new MorphaAnnotator(false));//lemma
		/**/
		pipeline.addAnnotator(new DependencyParseAnnotator());
		/**/
		pipeline.addAnnotator(new NERCombinerAnnotator(false));
		pipeline.addAnnotator(new ParserAnnotator(false, -1));
		//Properties props = new Properties();
		//pipeline.addAnnotator(new TimeAnnotator("sutime",props));
		log.info("Pipeline Initialized.");
	}
	
	/**Annotate a document, one document at a time.
	 * @param doc
	 * @return AnnotatedDoc
	 * @throws IOException 
	 * @throws Exception
	 */
	public AnnotatedDoc annotate(Document doc) throws IOException{
		log.info("Pipeline Annotating:"+doc.getsource());
		String text = doc.text();
		// create annotation with text
		
		Annotation document = new Annotation(text);
		AnnotatedDoc adoc = new AnnotatedDoc(doc.getmetadata());
		adoc.setSource(doc.getsource());
		// annotate text with pipeline
		pipeline.annotate(document);
		// demonstrate typical usage
		int j = 0;
		for (CoreMap sentence: document.get(CoreAnnotations.SentencesAnnotation.class)) {
			//get the tree for the sentence
			Tree tree = sentence.get(TreeAnnotation.class);
			SemanticGraph dep = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
			AnnotatedSentence asen = new AnnotatedSentence(adoc);
			// get the tokens for the sentence and iterate over them
			int i = 1;
			for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				// get token attributes
				AnnotatedToken at = new AnnotatedToken(token.get(TextAnnotation.class));
				at.setIndex(i);
				at.setRef_id(String.valueOf(j)+'-'+String.valueOf(i++));
				at.setPos(token.get(PartOfSpeechAnnotation.class));
				at.setLemma(token.get(LemmaAnnotation.class));
				at.setOffset(new Pair<Integer,Integer>(token.beginPosition(),token.endPosition()-1));
				at.setAnnotation(token);
				asen.add(at);
			}
			asen.setDeplist(dep.typedDependencies());
			asen.setParseTree(tree);
			asen.setAnnotation(sentence);
			adoc.add(asen);
			j++;
	    }
		//deal with entity
		int start = 0; int end = 0;
		org.jsoup.nodes.Document metaDoc = doc.getmetadata();
		for(Element entity:metaDoc.getElementsByTag("entity")){
			nlp.annotator.util.entity.ACeEntity aceE = new nlp.annotator.util.entity.ACeEntity();
			aceE.setType(entity.attr("TYPE"));
			aceE.setSubtype(entity.attr("SUBTYPE"));
			aceE.setClass(entity.attr("CLASS"));
			this.searchAddEm_Entity(aceE, adoc, entity);
			adoc.addEntity(aceE);
		}
		for(Element timex2:metaDoc.getElementsByTag("timex2")){
			nlp.annotator.util.entity.TimeEntity timeE = new nlp.annotator.util.entity.TimeEntity(timex2.attr("VAL"));
			this.searchAddEm_Time(timeE, adoc, timex2);
			adoc.addEntity(timeE);
		}
		for(Element value:metaDoc.getElementsByTag("value")){
			nlp.annotator.util.entity.Value valE = new nlp.annotator.util.entity.Value();
			valE.setType(value.attr("TYPE"));
			this.searchAddEm_Val(valE, adoc, value);
			adoc.addEntity(valE);
		}		
		log.info("Pipeline Annotated:"+doc.getsource());
		return adoc;
	}
	private void searchAddEm_Val(Value valE, AnnotatedDoc adoc, Element value) {
		int start = 0, end = 0;
		ArrayList<Pair<Integer,Integer>> q = new ArrayList<Pair<Integer,Integer>>();
		for(Element vmTag : value.getElementsByTag("value_mention")){
			EntityMention em = new EntityMention();
			Element extentseq = vmTag.getElementsByTag("extent").first().getElementsByTag("charseq").first();
			start = Integer.valueOf(extentseq.attr("START"));
			end = Integer.valueOf(extentseq.attr("END"));
			for(AnnotatedToken aToken:this.searchToken(adoc, start, end)){
				em.addToken(aToken);
			}
			if(!em.isEmpty())
			valE.addEntityMention(em);
		}
	}

	private void searchAddEm_Time(TimeEntity timeE, AnnotatedDoc adoc, Element timex2) {
		int start = 0, end = 0;
		ArrayList<Pair<Integer,Integer>> q = new ArrayList<Pair<Integer,Integer>>();
		for(Element tmTag : timex2.getElementsByTag("value_mention")){
			EntityMention em = new EntityMention();
			Element extentseq = tmTag.getElementsByTag("extent").first().getElementsByTag("charseq").first();
			start = Integer.valueOf(extentseq.attr("START"));
			end = Integer.valueOf(extentseq.attr("END"));
			for(AnnotatedToken aToken:this.searchToken(adoc, start, end)){
				em.addToken(aToken);
			}
			if(!em.isEmpty())
			timeE.addEntityMention(em);
		}
	}

	private void searchAddEm_Entity(nlp.annotator.util.entity.Entity entity,AnnotatedDoc adoc,Element tag){
		int start = 0, end = 0;
		ArrayList<Pair<Integer,Integer>> q = new ArrayList<Pair<Integer,Integer>>();
		for(Element emTag : tag.getElementsByTag("entity_mention")){
			EntityMention em = new EntityMention();
			Element extentseq = emTag.getElementsByTag("extent").first().getElementsByTag("charseq").first();
			start = Integer.valueOf(extentseq.attr("START"));
			end = Integer.valueOf(extentseq.attr("END"));
			for(AnnotatedToken aToken:this.searchToken(adoc, start, end)){
				em.addToken(aToken);
			}
			Element seq = emTag.getElementsByTag("head").first().getElementsByTag("charseq").first();
			start = Integer.valueOf(seq.attr("START"));
			end = Integer.valueOf(seq.attr("END"));
			for(AnnotatedToken aToken:this.searchToken(adoc, start, end)){
				em.addHead(aToken);
			}
			if(!em.isEmpty())
			entity.addEntityMention(em);
		}
	}
	private ArrayList<AnnotatedToken> searchToken(AnnotatedDoc adoc,int start,int end){
		ArrayList<AnnotatedToken> ans = new ArrayList<AnnotatedToken>();
		for(AnnotatedSentence aSen : adoc){
			Pair<Integer,Integer> temp = contain(aSen.getTokenByIndex(1).getOffset().getFirst(),
											aSen.getTokenByIndex(aSen.size()-1).getOffset().getSecond(),
										start,
									end);
			if(temp!=null){
				int b = 0, e = 0;
				for(b = 1; b < aSen.size();b++){
					Pair<Integer,Integer> off1 = aSen.getTokenByIndex(b).getOffset();
					if(off1.getFirst().compareTo(temp.getFirst()) >= 0)
						break;
				}
				for(e = aSen.size()-1; e >= 1; e--){
					Pair<Integer,Integer> off1 = aSen.getTokenByIndex(e).getOffset();
					if(off1.getSecond().compareTo(temp.getSecond()) <= 0)
						break;
				}
				for(int i = b; i <= e; i++){
					ans.add(aSen.getTokenByIndex(i));
				}
			}
		}
		return ans;
	}
	/** Assumes b1 < e1 and b2 < e2
	 * @param b1
	 * @param e1
	 * @param b2
	 * @param e2
	 * @return
	 */
	private Pair<Integer,Integer> contain(int b1, int e1, int b2, int e2){
		if(e2 < b1) return null;
		if(e2 >= b1 && e2 < e1 && b2 < b1) return new Pair(b1,e2);
		if(e2 >= b1 && e2 < e1 && b2 >= b1) return new Pair(b2,e2);
		if( e2 >= e1 && b2 < b1) return new Pair(b1,e1);
		if( e2 >= e1 && b2 >= b1 && b2 < e1) return new Pair(b2,e1);
		if( e2 >= e1 && b2 > e1) return null;
		return null;
	}
	public static void main(String[] args) throws Exception{
		Pipeline p = new Pipeline();
		Document d = new ACEDocument(new File("./data/ACE/bc/CNN_CF_20030303.1900.00.sgm"));
		AnnotatedDoc aDoc = p.annotate(d);
		LinkedList<nlp.annotator.util.entity.Entity> elist = aDoc.getEntitylist();
		System.out.println(elist.get(0));
	}
}
