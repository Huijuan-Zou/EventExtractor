package nlp.annotator.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import nlp.annotator.util.entity.EntityMention;

public class AnnotatedSentence implements Iterable<AnnotatedToken>{
	private ArrayList<AnnotatedToken> tokenlist;
	private AnnotatedDoc parent;
	private LinkedList<MyDependency> deplist;
	private ArrayList<EntityMention> mentions;
	private MyTree parsetree;
	private CoreMap annotation;
	public AnnotatedSentence(AnnotatedDoc p) {
		this.parent = p;
		this.tokenlist = new ArrayList<AnnotatedToken>();
		this.deplist = new LinkedList<MyDependency>();
		this.mentions = new ArrayList<EntityMention>();
		AnnotatedToken root = new AnnotatedToken("ROOT",this);
		root.setIndex(0);
		tokenlist.add(root);
		
	}
	public AnnotatedSentence() {
		this.parent = null;
		this.tokenlist = new ArrayList<AnnotatedToken>();
		this.deplist = new LinkedList<MyDependency>();
		this.mentions = new ArrayList<EntityMention>();
		AnnotatedToken root = new AnnotatedToken("ROOT",this);
		root.setIndex(0);
		tokenlist.add(root);
	}
	public ArrayList<EntityMention> getMentions(){
		return this.mentions;
	}
	public Boolean add(EntityMention em){
		return this.mentions.add(em);
	}
	public Boolean add(AnnotatedToken token){
		token.setParent(this);
		return tokenlist.add(token);
	}
	@Override
	public Iterator<AnnotatedToken> iterator() {
		// TODO Auto-generated method stub
		return tokenlist.iterator();
	}
	public AnnotatedDoc getParent() {
		return parent;
	}
	public void setParent(AnnotatedDoc doc){
		this.parent = doc;
	}
	public LinkedList<MyDependency> getDeplist() {
		return deplist;
	}
	public void setDeplist(LinkedList<MyDependency> deplist) {
		this.deplist = deplist;
	}
	public void setDeplist(Collection<TypedDependency> deplist){
		LinkedList<MyDependency> dl = new LinkedList<MyDependency>();
		for(TypedDependency tp: deplist){
			dl.add(new MyDependency(tokenlist.get(tp.gov().index()),
								this.tokenlist.get(tp.dep().index()),
								tp.reln().getShortName()));
		}
		this.deplist = dl;
	}
	public void setParseTree(MyTree tree){
		this.parsetree = tree;
	}
	public Boolean addDependency(MyDependency dep){
		return this.deplist.add(dep);
	}
	public AnnotatedToken getTokenByIndex(int index){
		return this.tokenlist.get(index);
	}
	private Boolean isLeaf(Tree t){
		return t.getChildrenAsList().size() == 1 && t.getChildrenAsList().get(0).isLeaf();
	}
	private int setParseTreeSub(MyTree target, Tree source,int k,int dep){
		int count = k;
		target.setTag(source.label().toString());
		if(this.isLeaf(source)){ 
			AnnotatedToken token = this.getTokenByIndex(count);
			token.setParseNode(target);
			target.setValue(token);
			count++;
			return count;
		}
		for(Tree sChild:source.getChildrenAsList())
		{
			MyTree tChild = new MyTree();
			count = this.setParseTreeSub(tChild, sChild,count,dep+1);
			tChild.setParent(target);
			target.addChild(tChild);
		}
		return count;
	}
	public void setParseTree(Tree tree){
		if(tree == null) return;
		this.parsetree = new MyTree();
		this.setParseTreeSub(this.parsetree, tree,1,0);
	}
	
	public MyTree getParseTree(){
		return this.parsetree;
	}

	public String toString()
    { 
		StringBuffer ans = new StringBuffer("[ ");
		for(AnnotatedToken at:tokenlist){
			ans.append(at.toString()+", ");
		}
		ans.append("]");
		return ans.toString();
    }
	private Boolean isSymbol(String s){
		return !(s.equals("(") || s.equals(")"));
	}
	public void setParseTree(String repre) {
		String[] syms = repre.split(" ");
		int limit = syms.length;
		int i = 0;
		LinkedList<MyTree> q = new LinkedList<MyTree>();
		// is leaf
		int index = 1;
		//System.out.println(syms);
		while( i < limit){
			//System.out.println(syms[i]);
			if(syms[i].equals("(")){
				if( isSymbol(syms[i+1]) && isSymbol(syms[i+2]) ){
					q.add(new MyTree(syms[i+1],this.getTokenByIndex(index++)));
					i = i + 3;
				}
				else{
					q.add(new MyTree(syms[i+1]));
					i = i + 2;
				}
			}else if ( syms[i].equals(")") && q.size() > 1 ){
				q.get(q.size()-2).addChild(q.removeLast());
				i++;
			}else
				i++;
		}
		this.setParseTree(q.getFirst());
	}
	public Integer size(){
		return this.tokenlist.size();
	}
	public CoreMap getAnnotation() {
		return annotation;
	}
	public void setAnnotation(CoreMap annotation) {
		this.annotation = annotation;
	}
}
