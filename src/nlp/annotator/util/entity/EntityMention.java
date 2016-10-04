package nlp.annotator.util.entity;

import java.util.ArrayList;

import nlp.annotator.util.AnnotatedToken;

public class EntityMention {
	private ArrayList<AnnotatedToken> tokens;
	private ArrayList<AnnotatedToken> head;
	private Entity parent;
	public EntityMention(){
		this.head =  new ArrayList<AnnotatedToken>();
		tokens = new ArrayList<AnnotatedToken>();
	}
	public int size(){
		return this.tokens.size();
	}
	public Boolean addToken(AnnotatedToken aT){
		return this.tokens.add(aT);
	}
	public AnnotatedToken getHeadByIndex(int i){
		return this.head.get(i);
	}
	public AnnotatedToken getTokenByIndex(int i){
		return this.tokens.get(i);
	}
	public Boolean isEmpty(){
		return this.tokens.isEmpty();
	}
	public void addHead(AnnotatedToken h) {
		this.head.add(h);
	}
	public String toString(){
		String ans = "head:";
		for(AnnotatedToken aToken: head){
			ans += (" " + aToken.toStringRefered());
		}
		ans += '\n';
		for(AnnotatedToken aToken: tokens){
			ans += (" " + aToken.toStringRefered());
		}
		ans += '\n';
		return ans;
	}
	public Entity getParent() {
		return parent;
	}
	public void setParent(Entity parent) {
		this.parent = parent;
	}
}
