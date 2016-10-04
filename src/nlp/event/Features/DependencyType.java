package nlp.event.Features;

import java.util.LinkedList;

import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.MyDependency;
import nlp.event.Feature.Feature;

public class DependencyType extends Feature {

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(AnnotatedToken t) {
		if(t.getIndex().equals(0))	return "-NULL-";
		AnnotatedSentence Sen=t.getParent();
		for (MyDependency md : Sen.getDeplist()){
			if (md.getDependent().equals(t)){
				return md.getType();	
			}
		}
		return "-NULL-";
	}

}
