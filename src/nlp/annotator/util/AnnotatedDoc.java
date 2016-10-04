package nlp.annotator.util;

import java.util.Iterator;
import java.util.LinkedList;
import nlp.annotator.util.entity.*;
public class AnnotatedDoc implements Iterable<AnnotatedSentence> {
	private LinkedList<AnnotatedSentence> senlist;
	private LinkedList<nlp.annotator.util.entity.Entity> entitylist;
	private org.jsoup.nodes.Document metadata;
	private String source;
	public AnnotatedDoc() {
		this.metadata = null;
		senlist = new LinkedList<AnnotatedSentence>();
		entitylist = new LinkedList<nlp.annotator.util.entity.Entity>();
	}
	public AnnotatedDoc(org.jsoup.nodes.Document metadata) {
		this.metadata = metadata;
		entitylist = new LinkedList<nlp.annotator.util.entity.Entity>();
		senlist = new LinkedList<AnnotatedSentence>();
	}
	public Boolean add(AnnotatedSentence sen){
		sen.setParent(this);
		return senlist.add(sen);
	}
	public Boolean addEntity(nlp.annotator.util.entity.Entity enti){
		return entitylist.add(enti);
	}
	public int entitysize(){
		return this.entitylist.size();
	}
	public int size(){
		return senlist.size();
	}
	@Override
	public Iterator<AnnotatedSentence> iterator() {
		// TODO Auto-generated method stub
		return senlist.iterator();
	}
	public org.jsoup.nodes.Document getMetadata() {
		return metadata;
	}
	public LinkedList<nlp.annotator.util.entity.Entity> getEntitylist(){
		return this.entitylist;
	}
	public void setMetadata(org.jsoup.nodes.Document metadata) {
		this.metadata = metadata;
	}
	public AnnotatedSentence getSenByIndex(int i){
		return this.senlist.get(i);
	}
	public String toString(){
		StringBuffer sb = new StringBuffer("{");
		for(AnnotatedSentence aSen: senlist){
			sb.append(aSen.toString()+'\n');
		}
		sb.append('\n');
		return sb.toString();
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
