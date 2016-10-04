package nlp.corpus.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

public class ACEDocument implements nlp.corpus.document.Document {
	private String source;
	private String doc;
	private String datetime;
	org.jsoup.nodes.Document metadata;
	public ACEDocument(File f) throws IOException {
		;
		metadata = Jsoup.parse(
				new String(Files.readAllBytes(Paths.get(f.getAbsolutePath().replace(".sgm", ".apf.xml"))), StandardCharsets.UTF_8), 
				"",
				Parser.xmlParser());
		FileInputStream fis = new FileInputStream(f);
		byte[] data = new byte[(int) f.length()];
		fis.read(data);
		fis.close();
		doc = new String(data, "UTF-8");
		int b = doc.indexOf("<DATETIME>")+10;
		int e = doc.lastIndexOf("</DATETIME>");
		this.setDatetime(doc.substring(b,e).trim());
		doc = doc.replaceAll("<((.*?)|((.*?(\\s.*?)+?)))>", "");
		source = f.getPath();
	}
	public String text(){
		return doc;
	}
	public String getsource(){
		return source;
	}
	public String toString(){
		return doc;
	}
	public org.jsoup.nodes.Document getmetadata(){
		return metadata;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public static void main(String[] args) throws IOException{
		ACEDocument t = new ACEDocument( new File("./data/ACE/bc/CNN_CF_20030303.1900.00.sgm") );
	}

}
