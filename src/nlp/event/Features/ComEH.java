package nlp.event.Features;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.*;
public class ComEH extends CompoundFeature{
	Feature f;
	public ComEH(){
		f = new WordDepth();
	}
	@Override
	public String getMineName() {
		// TODO Auto-generated method stub
		return "ComEH";
	}

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "full:lowfull:pos:dep";
	}

	@Override
	public String getValue(AnnotatedToken t) {
		// TODO Auto-generated method stub
		return t.getToken()+":"+t.getToken().toLowerCase()+":"+t.getPos()+":"+f.getValue(t);
	}

}
