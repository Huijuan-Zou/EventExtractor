package nlp.event.Features;

import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;

public class LemmaWord extends Feature {
	public LemmaWord(){
		
	}

	@Override
	public String getname() {
		
		return "lemmaWord";
	}
	@Override
	public String getValue(AnnotatedToken t) {
		if( t.getIndex().equals(0) ) return "-NULL-";
		return t.getLemma();
	}

	
	public void main(String[] args){
		
	}
}
