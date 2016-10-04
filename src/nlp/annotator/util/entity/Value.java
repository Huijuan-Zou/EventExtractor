package nlp.annotator.util.entity;

import java.util.ArrayList;

import nlp.annotator.util.AnnotatedToken;

public class Value implements Entity {
	ArrayList<AnnotatedToken> tokens;
	String type;
	private ArrayList<EntityMention> emList;
	public Value(){
		this.emList = new ArrayList<EntityMention>();
		this.tokens = new ArrayList<AnnotatedToken>();
		this.type = null;
	}
	public void setType(String type){
		this.type = type;
	}
	@Override
	public Boolean addToken(AnnotatedToken atoken) {
		atoken.addEntity(this);
		return this.tokens.add(atoken);
	}
	public String toString(){
		String re = this.type + '\n';
		for(EntityMention a : emList){
			re += (a.toString()+"\n");
		}
		return re;
	}
	@Override
	public Boolean addEntityMention(EntityMention em) {
		em.setParent(this);
		for(int i = 0; i< em.size();i++){
			em.getTokenByIndex(i).setTokenNE(this.getType());
		}
		em.getTokenByIndex(0).getParent().add(em);
		return this.emList.add(em);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
	@Override
	public int mentionsSize() {
		return this.emList.size();
	}
	public EntityMention getMentionByIndex(int i) {
		return this.emList.get(i);
	}
}
