package nlp.event.Feature;

import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.entity.EntityMention;

public interface PairFeature {
	abstract public String getname();
	abstract public String getValue(AnnotatedToken t,EntityMention em);
}
