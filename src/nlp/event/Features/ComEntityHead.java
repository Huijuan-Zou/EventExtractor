package nlp.event.Features;

import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.entity.EntityMention;
import nlp.event.Feature.CompoundFeature;
import nlp.event.Feature.PairFeature;

public class ComEntityHead implements PairFeature{
	private CompoundFeature cf = new ComEH();
	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "ComEntityHead";
	}

	@Override
	public String getValue(AnnotatedToken t, EntityMention em) {
		// TODO Auto-generated method stub
		return null;
	}

}
