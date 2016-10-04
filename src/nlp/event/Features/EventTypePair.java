package nlp.event.Features;

import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.entity.EntityMention;
import nlp.event.Feature.Feature;
import nlp.event.Feature.PairFeature;

public class EventTypePair implements PairFeature{
	Feature f;
	public EventTypePair(){
		f = new EventType();
	}
	@Override
	public String getname() {
		return "EventTypePair";
	}

	@Override
	public String getValue(AnnotatedToken t, EntityMention em) {
		return f.getValue(t);
	}

}
