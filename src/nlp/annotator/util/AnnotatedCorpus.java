package nlp.annotator.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import edu.stanford.nlp.io.EncodingPrintWriter.out;
import edu.stanford.nlp.time.SUTime.Temporal;
import nlp.annotator.pipeline.AnnotateAutomator;
import nlp.annotator.util.entity.EntityMention;
import nlp.annotator.util.entity.TimeEntity;
import nlp.annotator.util.entity.Value;
import nlp.corpus.ACECorpus;
import nlp.corpus.Corpus;
import nlp.event.extractor.Extractor;
import nlp.util.Pair;
	
public class AnnotatedCorpus {
	private LinkedList<AnnotatedDoc> doclist;
	private final Logger log = Logger.getLogger(AnnotatedCorpus.class.getName());
	public Integer size(){
		return doclist.size();
	}
	public void setDoclist(LinkedList<AnnotatedDoc> doclist){
		this.doclist = doclist;
	}
	public LinkedList<AnnotatedDoc> getDoclist(){
		return this.doclist;
	}
	public void saveOneDoc(AnnotatedDoc aDoc, String p) throws IOException{
		FileWriter fw = new FileWriter(new File(p+".meta"));
		if(aDoc.getMetadata()==null)
			fw.write("");
		else
			fw.write(aDoc.getMetadata().toString());
		fw.close();
		org.jsoup.nodes.Document doc = Jsoup.parse("<doc></doc>");
		Element docu = doc.getElementsByTag("doc").first();
		docu.attr("metapath", p+".meta");
		for(AnnotatedSentence aSen: aDoc){
			Element sen = new Element(Tag.valueOf("sentence"),"");
			//parseTree
			Element parseTree = new Element(Tag.valueOf("parseTree"),"");
			parseTree.appendText(aSen.getParseTree().toString());
			// deplist
			for(MyDependency dep : aSen.getDeplist()){
				Element depTag = new Element(Tag.valueOf("dependency"),"");
				depTag.attr("head", dep.getHead().toStringIndexed());
				depTag.attr("dependent",dep.getDependent().toStringIndexed());
				depTag.attr("type",dep.getType());
				sen.appendChild(depTag);
				}
			sen.appendChild(parseTree);
			
			int K = 1;
			Iterator<AnnotatedToken> it = aSen.iterator();
			it.next();
			while(it.hasNext()){
				AnnotatedToken aToken = it.next();
				Element token = new Element(Tag.valueOf("Token"),"");
				token.attr("token", aToken.getToken());
				token.attr("lemma", aToken.getLemma());
				token.attr("pos", aToken.getPos());
				token.attr("ne", aToken.getTokenNE());
				token.attr("offset", aToken.getOffset().toString());
				token.attr("index", String.valueOf(K++));
				token.attr("id", aToken.getRef_id());
				sen.appendChild(token);
			}
			docu.appendChild(sen);
		}
		fw = new FileWriter(new File(p+".data"));
		fw.write(docu.toString());
		fw.close();
	}
	public void save(File f) throws IOException {
		log.info("Begin to save.");
		f.mkdirs();
		int i = 0;
		for(AnnotatedDoc aDoc: doclist){
			String p = f.getPath()+'/'+ (i++);
			saveOneDoc(aDoc,p);
		}
		log.info("Save finished.");
	}
	private LinkedList<File> getFileList(File strPath) {
		File[] files = strPath.listFiles(); // 该文件目录下文件全部放入数组
		LinkedList<File> filelist = new LinkedList<File>();
		if (files != null){
			for (File f: files) {
				if (f.isDirectory()) { // 判断是文件还是文件夹
					filelist.addAll(this.getFileList(f));
				} else if (f.getName().endsWith(".data")) { // 判断文件名是否以.sgm结尾
					filelist.add(f);
				}
			}
		}
		return filelist;
	}
	
	public AnnotatedDoc readOneDoc(File f) throws IOException{	
		org.jsoup.nodes.Document doc = Jsoup.parse(f,"UTF-8");
		AnnotatedDoc aDoc = new AnnotatedDoc();
		for(Element sen:doc.getElementsByTag("sentence")){
			AnnotatedSentence aSen = new AnnotatedSentence();
			for(Element token:sen.getElementsByTag("token")){
				AnnotatedToken aToken = new AnnotatedToken(token.attr("token"), aSen);
				aToken.setLemma(token.attr("lemma"));
				aToken.setPos(token.attr("pos"));
				aToken.setTokenNE("O");
				aToken.setIndex(Integer.valueOf(token.attr("index")));
				aToken.setRef_id(token.attr("ID"));
				//TODO offset	
				aToken.setOffset(Pair.valueOf(token.attr("offset")));
				aSen.add(aToken);			
			}
			//DEP LIST
			for(Element dep:sen.getElementsByTag("dependency")){
				String[] tempH = dep.attr("head").split("-");
				String[] tempD = dep.attr("dependent").split("-");
				MyDependency t = new MyDependency(aSen.getTokenByIndex(Integer.valueOf(tempH[tempH.length-1])), 
						 aSen.getTokenByIndex(Integer.valueOf(tempD[tempD.length-1])), 
						  dep.attr("type"));
				aSen.addDependency(t);
			}
			//PARSE TREE
			aSen.setParseTree(sen.getElementsByTag("parsetree").first().text().trim());
			aDoc.add(aSen);
		}
		int start = 0; int end = 0;
		String absP = f.getAbsolutePath();
		File metaf = new File(absP.substring(0, absP.lastIndexOf('.')) + ".meta");
		aDoc.setMetadata(Jsoup.parse(metaf, "UTF-8"));
		org.jsoup.nodes.Document metaDoc = aDoc.getMetadata();
		for(Element entity:metaDoc.getElementsByTag("entity")){
			nlp.annotator.util.entity.ACeEntity aceE = new nlp.annotator.util.entity.ACeEntity();
			aceE.setType(entity.attr("TYPE"));
			aceE.setSubtype(entity.attr("SUBTYPE"));
			aceE.setClass(entity.attr("CLASS"));
			this.searchAddEm_Entity(aceE, aDoc, entity);
			aDoc.addEntity(aceE);
		}
		for(Element timex2:metaDoc.getElementsByTag("timex2")){
			nlp.annotator.util.entity.TimeEntity timeE = new nlp.annotator.util.entity.TimeEntity(timex2.attr("VAL"));
			this.searchAddEm_Time(timeE, aDoc, timex2);
			aDoc.addEntity(timeE);
		}
		for(Element value:metaDoc.getElementsByTag("value")){
			nlp.annotator.util.entity.Value valE = new nlp.annotator.util.entity.Value();
			valE.setType(value.attr("TYPE"));
			this.searchAddEm_Val(valE, aDoc, value);
			aDoc.addEntity(valE);
		}	
		return aDoc;
	}	
	public void read(File f) throws IOException {
		log.info("Reading begin.");
		this.doclist = new LinkedList<AnnotatedDoc>();
		LinkedList<File> flist = this.getFileList(f);
		for(File docf : flist){
			this.doclist.add(readOneDoc(docf));
		}
		log.info("Reading Finished.");
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
			Elements hs = emTag.getElementsByTag("head");
			if(!hs.isEmpty()){
				Element seq = hs.first().getElementsByTag("charseq").first();
				start = Integer.valueOf(seq.attr("START"));
				end = Integer.valueOf(seq.attr("END"));
				for(AnnotatedToken aToken:this.searchToken(adoc, start, end)){
					em.addHead(aToken);
				}
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
		PropertyConfigurator.configure("log4j.properties");
		AnnotatedCorpus aCS = new AnnotatedCorpus();
		Hashtable<String,Integer> h = new Hashtable<String,Integer>();
		aCS.read(new File("./data/AnnotatedFile/"));
		for(AnnotatedDoc aDoc:aCS.getDoclist()){
			for(AnnotatedSentence aSen:aDoc){
				Iterator<AnnotatedToken> it = aSen.iterator();
				it.next();
				while(it.hasNext()){
					String flag = it.next().getTokenNE();
					if(h.get(flag)==null)
						h.put(flag, 1);
				}
			}
		}
		Enumeration<String> keys = h.keys();
		while(keys.hasMoreElements()){
			System.out.println(keys.nextElement());
		}
	}
}
