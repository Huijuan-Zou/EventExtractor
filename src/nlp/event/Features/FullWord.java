
package nlp.event.Features;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;

public class FullWord extends Feature{
	public FullWord(){
		
	}

	@Override
	public String getname() {
		
		return "token";
	}

	@Override
	public String getValue(AnnotatedToken t) {
		
		return t.getToken();
	}


}