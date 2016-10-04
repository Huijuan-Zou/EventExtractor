package nlp.event.Feature;

import org.jsoup.nodes.Document;

import net.didion.jwnl.JWNLException;
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;

public abstract class Feature {

	public Feature() {
		// TODO Auto-generated constructor stub
	}
	abstract public String getname();
	abstract public String getValue(AnnotatedToken t);
}
