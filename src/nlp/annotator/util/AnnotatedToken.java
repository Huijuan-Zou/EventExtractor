package nlp.annotator.util;

import java.util.Hashtable;
import java.util.LinkedList;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.time.SUTime.Temporal;
import nlp.util.Pair;

public class AnnotatedToken {
	private final String token;
	private String pos;
	private String lemma;
	private String tokenNE;
	private Pair<Integer,Integer> offset;
	private Integer index;
	private AnnotatedSentence parent;
	private MyTree parseNode;
	private String event_type;
	private CoreLabel annotation;
	private Temporal time;
	private String ref_id;
	private LinkedList<nlp.annotator.util.entity.Entity> entitylist;

	public AnnotatedToken(String token,AnnotatedSentence parent) {
		entitylist = new LinkedList<nlp.annotator.util.entity.Entity>();
		this.setRef_id(null);
		this.setTime(null);
		this.event_type = "NOTEVENT";
		this.parent = parent;
		this.token = token;
		this.setPos(null);
		this.setLemma(null);
		this.setTokenNE(null);
		this.offset = null;
	}
	public AnnotatedToken(String token) {
		entitylist = new LinkedList<nlp.annotator.util.entity.Entity>();
		this.setRef_id(null);
		this.setTime(null);
		this.event_type = "NOTEVENT";
		this.parent = null;
		this.token = token;
		this.setPos(null);
		this.setLemma(null);
		this.setTokenNE(null);
		this.offset = null;
	}
	public String getTokenNE() {
		return tokenNE;
	}

	public void setTokenNE(String tokenNE) {
		this.tokenNE = tokenNE;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Pair<Integer,Integer> getOffset() {
		return offset;
	}

	public void setOffset(Pair<Integer,Integer> offset) {
		this.offset = offset;
	}

	public String getToken() {
		return token;
	}

	public AnnotatedSentence getParent() {
		return parent;
	}
	public void setParent(AnnotatedSentence p) {
		this.parent = p;
	}
	public String toString()
    { 
           return this.token; 
    }
	public String toStringIndexed()
    { 
           return this.token +"-"+this.getIndex(); 
    }
	public String toStringRefered(){
		return this.token +":"+this.getRef_id();
	}
	public MyTree getParseNode() {
		return parseNode;
	}
	public void setParseNode(MyTree parseNode) {
		this.parseNode = parseNode;
	}
	public Boolean equals(AnnotatedToken other){
		return other.parent == this.getParent() && this.getOffset().equals(other.getOffset());
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public void setIndex(int index) {
		this.index = new Integer(index);
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public CoreLabel getAnnotation() {
		return annotation;
	}
	public void setAnnotation(CoreLabel annotation) {
		this.annotation = annotation;
	}
	public Temporal getTime() {
		return time;
	}
	public void setTime(Temporal time) {
		this.time = time;
	}
	public String getRef_id() {
		return ref_id;
	}
	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}
	public Boolean addEntity(nlp.annotator.util.entity.Entity en){
		return this.entitylist.add(en);
	}
}
