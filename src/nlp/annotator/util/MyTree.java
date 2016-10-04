package nlp.annotator.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MyTree {
	private ArrayList<MyTree> children;
	private String tag;
	private AnnotatedToken value;
	private MyTree parent;
	public MyTree(){
		children = new ArrayList<MyTree>();
	}
	
	public MyTree(String tag){
		this.tag = tag;
		children = new ArrayList<MyTree>();
	}
	public MyTree(String tag,AnnotatedToken value) {
		this.tag = tag;
		this.value = value;
		value.setParseNode(this);
		children = new ArrayList<MyTree>();
	}
	public Boolean addChild(MyTree child){
		child.setParent(this);
		return this.children.add(child);
	}
	public ArrayList<MyTree> getChildren(){
		return this.children;
	}
	public String getTag(){
		return this.tag;
	}
	public void setTag(String tag){
		this.tag = tag;
	}
	public AnnotatedToken getValue(){
		return this.value;
	}
	public void setValue(AnnotatedToken value){
		this.value = value;
	}
	public Boolean isLeaf(){
		return this.getValue() == null;
	}
	public MyTree getParent() {
		return parent;
	}
	public void setParent(MyTree parent) {
		this.parent = parent;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer("( ");
		sb.append(this.getTag());
		
		if(this.getValue()==null){
			for(MyTree child:this.getChildren()){
				sb.append(' ');
				sb.append(child);
			}
		}
		else{
			sb.append(' ');
			sb.append(this.getValue());
		}
		sb.append(" )");
		return sb.toString();
	}
}
