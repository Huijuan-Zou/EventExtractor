package nlp.event.Features;

import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;

public class Offset extends Feature {

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(AnnotatedToken t) {
		// TODO Auto-generated method stub
		return t.getOffset().toString();
	}

}
