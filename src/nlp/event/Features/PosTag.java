package nlp.event.Features;

import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;

public class PosTag extends Feature{

	public PosTag() {
		// TODO Auto-generated constructor stub
	}
	public String getname(){
		return "Postag";
	}
	public String getValue(AnnotatedToken t) {
		// TODO Auto-generated method stub
		if(t.getIndex().equals(0)) return "-NULL-";
		return t.getPos();
	}
}
