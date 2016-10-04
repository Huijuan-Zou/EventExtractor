package nlp.annotator.util.entity;

import java.util.ArrayList;

import nlp.annotator.util.AnnotatedToken;

public class ACeEntity implements Entity {
	ArrayList<AnnotatedToken> tokens;
	ArrayList<EntityMention> emList;
	private String type;
	private String subtype;
	private String classs;
	public ACeEntity(){
		this.emList = new ArrayList<EntityMention>();
		this.tokens = new ArrayList<AnnotatedToken>();
		this.type = null;
		this.subtype = null;
		this.classs = null;
	}
	@Override
	public Boolean addToken(AnnotatedToken atoken) {
		atoken.addEntity(this);
		return this.tokens.add(atoken);
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getClasss() {
		return classs;
	}
	public void setClass(String classs) {
		this.classs = classs;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}
	public String toString(){
		String re = this.type + "-" + this.subtype + '\n';
		for(EntityMention a : emList){
			re += (a.toString()+"\n");
		}
		return re;
	}
	@Override
	public Boolean addEntityMention(EntityMention em) {
		em.setParent(this);
		em.getTokenByIndex(0).getParent().add(em);
		for(int i = 0; i< em.size();i++){
			em.getTokenByIndex(i).setTokenNE(this.getType());
		}
		return this.emList.add(em);
	}
	@Override
	public int mentionsSize() {
		return this.emList.size();
	}
	@Override
	public EntityMention getMentionByIndex(int i) {
		return this.emList.get(i);
	}
}
