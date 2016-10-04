package nlp.annotator.util;

public class MyDependency {
	private AnnotatedToken head;
	private AnnotatedToken dependent;
	private String type;
	public MyDependency(AnnotatedToken head,AnnotatedToken dependent,String type) {
		this.head = head;
		this.dependent = dependent;
		this.type = type;
	}
	public AnnotatedToken getHead(){
		return head;
	}
	public AnnotatedToken getDependent(){
		return dependent;
	}
	public String getType(){
		return type;
	}
	public String toString(){
		return type + ":" + head + "==>" + dependent;
	}
}
