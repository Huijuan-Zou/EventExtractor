package nlp.event.Features;

import java.util.Iterator;


import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.CompoundFeature;

public class RightContextPosTag extends CompoundFeature{


	public RightContextPosTag() {
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
		String firstPosTag = "-NULL-";
		AnnotatedToken second = null;
		String secPosTag = "-NULL-";
		AnnotatedToken third = null;
		String thirdPosTag = "-NULL-";
		
		while (!current.equals(t)){
			current = (AnnotatedToken) it.next();	
		}
		
		if(it.hasNext()){
			first = (AnnotatedToken) it.next();
			firstPosTag = first.getPos();
			if(it.hasNext()){
				second = (AnnotatedToken) it.next();
				secPosTag = second.getPos();
				if(it.hasNext()){
					third = (AnnotatedToken) it.next();
					thirdPosTag = third.getPos();
				}
			}
		}
		

		return firstPosTag.replace(':', '~') + ":" + secPosTag.replace(':', '~') + ":" + thirdPosTag.replace(':', '~');
	}

	@Override
	public String getMineName() {
		// TODO Auto-generated method stub
		return "RightContextPosTag";
	}

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "RCPT1:PCPT2:RCPT3";

	}

}
