package nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import nlp.corpus.ACECorpus;
import nlp.corpus.document.ACEDocument;
import nlp.corpus.document.Document;

public class ACECorpus implements Corpus {

	private File rootdir;
	private LinkedList<nlp.corpus.document.ACEDocument> doclist; 
	
	public ACECorpus(String src){
		try{
			rootdir = new File(src);

			doclist = new LinkedList<nlp.corpus.document.ACEDocument>();
			System.out.println(doclist.size());
			for(File d : this.getFileList(rootdir))
				doclist.add(new ACEDocument(d));
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	private LinkedList<File> getFileList(File strPath) {
		File[] files = strPath.listFiles(); // 该文件目录下文件全部放入数组
		LinkedList<File> filelist = new LinkedList<File>();
		if (files != null){
			for (File f: files) {
				if (f.isDirectory()) { // 判断是文件还是文件夹
					filelist.addAll(this.getFileList(f));
				} else if (f.getName().endsWith(".sgm")) { // 判断文件名是否以.sgm结尾
					filelist.add(f);
				}
			}
		}
		return filelist;
	}
	/* (non-Javadoc)
	 * @see nlp.corpus.Corpus#nextDocument()
	 */
	@Override
	public Document nextDocument() {
		// TODO Auto-generated method stub
		return doclist.remove();
	}
	public static void main(String[] args) throws IOException{
		ACECorpus t = new ACECorpus("./data/ACE");
	}

	@Override
	public Boolean empty() {
		return doclist.isEmpty();
	}

	@Override
	public Integer size() {
		// TODO Auto-generated method stub
		return this.doclist.size();
	}
	@Override
	public Document getDocByIndex(int index) {
		return this.doclist.get(index);
	}

}
