package nlp.event.Features;
import nlp.annotator.util.AnnotatedToken;

import java.util.Iterator;

import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.MyTree;
import nlp.event.Feature.Feature;


public class WordDepth extends Feature {

	public WordDepth (){
		
	}
	@Override
	public String getname() {
		
		return "WordDepth";
	}
	
	@Override
	public String getValue(AnnotatedToken t) {
		MyTree node = t.getParseNode();
		if(node == null) return "-NULL-";
		int depth = 0;
		while(node!=null){
			node = node.getParent();
			depth++;
		}
		return Integer.toString(depth);
	}
}
