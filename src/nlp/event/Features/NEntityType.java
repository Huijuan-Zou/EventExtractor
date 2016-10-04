package nlp.event.Features;

import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;

public class NEntityType extends Feature {

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "NEntityType";
	}

	@Override
	public String getValue(AnnotatedToken t) {
		if(t.getTokenNE()!=null) return t.getTokenNE();
		return "-NULL-";
	}

}
