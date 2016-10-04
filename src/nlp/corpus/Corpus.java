package nlp.corpus;

import java.io.File;

import nlp.corpus.document.Document;

public interface Corpus {
	/** get Document in a destructive way, all document retrived will be delete from corpus
	 * @return
	 */
	public Document nextDocument();
	/**get document by index
	 * @param index
	 * @return
	 */
	public Document getDocByIndex(int index);
	public Boolean empty();
	public Integer size();
}
