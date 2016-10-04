package nlp.event.Features;
import java.util.LinkedList;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.MyDependency;
import nlp.event.Feature.Feature;

public class HeadWord extends Feature {

	
	public HeadWord (){
			
		
	}
	@Override
	public String getname() {
		
		return "HeadWord";
	}
	@Override
	public String getValue(AnnotatedToken t) {
		if(t.getIndex().equals(0))	return "-NULL-";
		AnnotatedSentence Sen=t.getParent();
		LinkedList<MyDependency> depList=Sen.getDeplist();
		for (int i=0;i<depList.size();i++){
			MyDependency md=depList.get(i);
			if (md.getDependent().equals(t)){
				return md.getHead().getToken();	
			}
		}
		return "-NULL-";
	}


	
	public void main(String[] args){
		
	}	
	
}