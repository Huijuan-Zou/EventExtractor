package nlp.event.Features;


import java.util.Iterator;

import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.CompoundFeature;

public class RightContextLowCase extends CompoundFeature{

	public RightContextLowCase() {
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
		
		while (!current.equals(t)){
			current = (AnnotatedToken) it.next();	
		}
		
		if(it.hasNext()){
			first = (AnnotatedToken) it.next();
			firstLC = first.getToken().toLowerCase();
			if(it.hasNext()){
				second = (AnnotatedToken) it.next();
				secLC = second.getToken().toLowerCase();
				if(it.hasNext()){
					third = (AnnotatedToken) it.next();
					thirdLC = third.getToken().toLowerCase();
				}
			}
		}
		return firstLC.replace(':', '~') + ":" + secLC.replace(':', '~') + ":" + thirdLC.replace(':', '~');
	}

	@Override
	public String getMineName() {
		// TODO Auto-generated method stub
		return "RightContextLowerCase";
	}

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "RCLC1:RCLC2:RCLC3";
	}
}
