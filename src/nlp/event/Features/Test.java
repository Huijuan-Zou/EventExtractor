package nlp.event.Features;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import net.didion.jwnl.JWNLException;
import nlp.annotator.pipeline.AnnotateAutomator;
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.MyDependency;
import nlp.annotator.util.AnnotatedSentence;
import nlp.corpus.ACECorpus;

public class Test {
	public static void main(String[] args) throws ClassNotFoundException, IOException, JWNLException{

		AnnotateAutomator aAutomator = new AnnotateAutomator(true);
				
		aAutomator.setCorpus(new ACECorpus("./data/ACE/bc/1"));
		
		aAutomator.annotate();
		WordNet wn = new WordNet();	
		AnnotatedDoc aDoc = aAutomator.getAnnotatedDoc();
		Iterator<AnnotatedSentence> it = aDoc.iterator();
		while(it.hasNext()){
			AnnotatedSentence sen = it.next();
			Iterator<AnnotatedToken> itt=sen.iterator();
			itt.next();
			while(itt.hasNext()){
				AnnotatedToken toke=itt.next();
				
				LeftContextLowCase lclc = new LeftContextLowCase();
				System.out.println(toke.getToken()+"	"+lclc.getname() + "	"+ lclc.getValue(toke));
					
				System.out.println(toke.getToken() + "	" + toke.getPos() + "	"+ wn.getValue(toke));
				
				LeftContextPosTag lcpt = new LeftContextPosTag();
				System.out.println(toke.getToken()+"	"+lcpt.getname() + "	"+ lcpt.getValue(toke));
				
				RightContextLowCase rclc = new RightContextLowCase();
				System.out.println(toke.getToken()+"	"+rclc.getname() + "	"+ rclc.getValue(toke));
				
				RightContextPosTag rcpt = new RightContextPosTag();
				System.out.println(toke.getToken()+"	"+rcpt.getname() + "	"+ rcpt.getValue(toke));
				System.in.read();
			}
		}	
	}
}
