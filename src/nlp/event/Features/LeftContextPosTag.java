package nlp.event.Features;

import java.util.Iterator;

import org.jsoup.nodes.Document;

import edu.stanford.nlp.maxent.Features;
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;

import nlp.event.Feature.CompoundFeature;
import nlp.event.Feature.Feature;

public class LeftContextPosTag extends CompoundFeature{


	public LeftContextPosTag() {
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
		
		while(!current.equals(t)){
			third = second;
			if (third!= null)
				thirdPosTag = third.getPos();
			second = first;
			if (second!= null)
				secPosTag = second.getPos();
			first = current;
			if (first!= null)
				firstPosTag = first.getPos();
			current = (AnnotatedToken) it.next();
		}							
		return thirdPosTag.replace(':', '~') + ":" + secPosTag.replace(':', '~') + ":" + firstPosTag.replace(':', '~');
	}

	@Override
	public String getMineName() {
		// TODO Auto-generated method stub
		return "LeftContextPosTag";
	}

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "LCPT3:LCPT2:LCPT1";
	}
	
	

}
