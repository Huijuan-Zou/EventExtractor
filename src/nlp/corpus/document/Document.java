package nlp.corpus.document;

public interface Document {
	public String text();
	public String getsource();
	public String toString();
	public String getDatetime();
	public org.jsoup.nodes.Document getmetadata();
}
