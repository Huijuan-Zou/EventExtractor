package nlp.event.Features;

import java.util.Iterator;

import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.CompoundFeature;

public class LeftContextLowCase extends CompoundFeature{

	public LeftContextLowCase() {
		// TODO Auto-generated constructor stub
	}
	
	public String getValue(AnnotatedToken t){
		
		if(t.getIndex().equals(0))	
			return "-NULL-:-NULL-:-NULL-";
		
		AnnotatedSentence s = t.getParent();
		Iterator<AnnotatedToken> it = s.iterator();		
		AnnotatedToken current = it.next();
		
		if (current.getIndex().equals(0)) 
			current = it.next();

		AnnotatedToken first = null;
		String firstLC = "-NULL-";
		AnnotatedToken second = null;
		String secLC = "-NULL-";
		AnnotatedToken third = null;
		String thirdLC = "-NULL-";
		
		while(!current.equals(t)){
			third = second;
			if (third!=null){
				thirdLC = third.getToken().toLowerCase();
			}
			second = first;
			if (second!=null){
				secLC = second.getToken().toLowerCase();
			}
			first = current;
			if (first!=null){
				firstLC = first.getToken().toLowerCase();
			}
			current = (AnnotatedToken) it.next();
		}							
		return thirdLC.replace(':', '~') + ":" + secLC.replace(':', '~') + ":" + firstLC.replace(':', '~');
	}

	@Override
	public String getMineName() {
		// TODO Auto-generated method stub
			return "LeftContextLowCase";
	}

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "LCLC3:LCLC2:LCLC1";
	}

}
