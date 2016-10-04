package nlp.event.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import nlp.annotator.pipeline.AnnotateAutomator;
import nlp.annotator.util.AnnotatedCorpus;
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.corpus.ACECorpus;
import nlp.corpus.document.format.TimblFormat;
import nlp.event.Feature.FeatureBuilder;
import nlp.event.Features.EventType;
import nlp.event.Features.IsArgument;
import nlp.model.Timbl;
import nlp.event.Feature.*;
public class Extractor {
	private AnnotateAutomator aAutomator;
	AnchorIdentifier ai;
	ArgumentIdentifier argI;
	private final Logger log = Logger.getLogger(Extractor.class.getName());
	public Extractor() {
		this.ai = null;
		try {
			//String[] p = {"yes"};
			String[] mp = EventType.pValues;
			ai = new AnchorIdentifier(new Timbl(mp));
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private void AnchorIdentification() throws ClassNotFoundException, IOException{
		String[] fs= {"FullWord","LcWord","LemmaWord","PosTag","WordDepth"//lexical features
						,"DependencyType","HeadWord","HeadPos","HeadType"//dependency features
						,"NEntityType"
						,"WordNet"//wordnet feature
						};
		String[] cfs = {
				//"LeftContextLowCase","LeftContextPosTag",//left context
				//"RightContextLowCase","RightContextPosTag",//right context
				//"NumDependent",
				"LabelDependent",
				//"ConstituteHead",
				//"NumTypeT",
				"LenPath",
				//"RelatedEntity"//related entity features
				};
		FeatureBuilder fb = new FeatureBuilder(fs,cfs,"EventType",new TimblFormat());
		AnnotatedCorpus aCorp = new AnnotatedCorpus();
		
		aCorp.read(new File("./data/AnnotatedFile/Train"));
		fb.output("./data/annotatedCorpus_train", true, aCorp.getDoclist());
		
		aCorp.read(new File("./data/AnnotatedFile/Test"));
		fb.output("./data/annotatedCorpus_test", true, aCorp.getDoclist());
		
		ai.train("data/annotatedCorpus_train");
		ai.predict("data/annotatedCorpus_test");
	}
	@SuppressWarnings("unused")
	private void ArgumentIdentification() throws IOException{
		String[] fs= {};
		String[] comfs = {""};
		String[] pairfs = {"IsArgument"};
		FeatureBuilder fb = new FeatureBuilder(fs,comfs,pairfs,new IsArgument(),new TimblFormat());
		AnnotatedCorpus aCorp = new AnnotatedCorpus();
		aCorp.read(new File("./data/AnnotatedFile/Train"));
		
		fb.output("./data/annotatedCorpus_train", true, aCorp.getDoclist());
		
		aCorp.read(new File("./data/AnnotatedFile/Test"));
		fb.output("./data/annotatedCorpus_test", true, aCorp.getDoclist());
		
		ai.train("data/annotatedCorpus_train");
		ai.predict("data/annotatedCorpus_test");
	}
	/**Train a model on source file f
	 * @param f source file
	 * @param tarinratio	training file ration, determines how many file will be used for training, the rest
	 * of it will be used for test
	 */
	public void train(File f, Float tarinratio){
		
	}
	/**Execute event extraction for all file in target directory, target file should be stored in html format
	 * content in <body> tag will be analyzed. After extraction, event information will added to this file
	 * in <events><event><event>...</events> format
	 * @param f
	 */
	public void run(File f){
		
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		PropertyConfigurator.configure("log4j.properties");
		new Extractor().AnchorIdentification();
		/*AnnotateAutomator a = new AnnotateAutomator(false);
		ACECorpus corp = new ACECorpus("./data/small");
		//System.out.println(corp.getDocByIndex(0));
		a.setCorpus(corp);
		a.annotate();
		System.out.println(a.getaCorpus().peekFirst());
		String[] fs = {"FullWord","Offset","IsAnchorIdentifier"};
		FeatureBuilder fb = new FeatureBuilder(fs,"IsAnchorIdentifier",new TimblFormat());
		fb.output("./data/small/check", false, a.getaCorpus());*/
	}
}
