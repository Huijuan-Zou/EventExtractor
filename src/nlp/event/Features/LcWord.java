package nlp.event.Features;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;

public class LcWord extends Feature{
	public LcWord(){
		
	}

	@Override
	public String getname() {
		
		return "lcWord";
	}
	@Override
	public String getValue(AnnotatedToken t) {
		if(t.getIndex().equals(0))	return "-NULL-";
		return t.getToken().toLowerCase();
	}
	
	public void main(String[] args){
		
	}
}