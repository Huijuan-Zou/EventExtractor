package nlp.annotator.util.entity;

import java.util.ArrayList;

import nlp.annotator.util.AnnotatedToken;

public class TimeEntity implements Entity {
	private String time;
	private ArrayList<AnnotatedToken> tokens;
	private ArrayList<EntityMention> emList;
	public TimeEntity(String t){
		this.emList = new ArrayList<EntityMention>();
		this.setTime(t);
		tokens = new ArrayList<AnnotatedToken>();
	}
	@Override
	public String getType(){
		return "TIME";
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Boolean addToken(AnnotatedToken t){
		t.addEntity(this);
		return this.tokens.add(t);
	}
	public ArrayList<AnnotatedToken> getTokens(){
		return this.tokens;
	}
	public String toString(){
		String re = this.time + '\n';
		for(EntityMention a : emList){
			re += (a.toString()+"\n");
		}
		return re;
	}
	@Override
	public Boolean addEntityMention(EntityMention em) {
		em.getTokenByIndex(0).getParent().add(em);
		em.setParent(this);
		for(int i = 0; i< em.size();i++){
			em.getTokenByIndex(i).setTokenNE(this.getType());
		}
		return this.emList.add(em);
	}
	@Override
	public int mentionsSize() {
		return this.emList.size();
	}	
	public EntityMention getMentionByIndex(int i) {
		return this.emList.get(i);
	}
}
