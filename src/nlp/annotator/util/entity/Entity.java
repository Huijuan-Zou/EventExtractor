package nlp.annotator.util.entity;

import nlp.annotator.util.AnnotatedToken;

public interface Entity {
	public String getType();
	public Boolean addToken(AnnotatedToken atoken);
	public Boolean addEntityMention(EntityMention em);
	public EntityMention getMentionByIndex(int i);
	public int mentionsSize();
}
