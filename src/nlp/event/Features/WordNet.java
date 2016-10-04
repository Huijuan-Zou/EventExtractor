package nlp.event.Features;

import java.io.FileInputStream;

import edu.stanford.nlp.maxent.Features;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import nlp.annotator.util.AnnotatedToken;

import nlp.event.Feature.Feature;

public class WordNet extends Feature{
	public Dictionary dict;

	public WordNet(){
		configureJWordNet();
		dict = Dictionary.getInstance();
	}
	
	public String getname(){
		return "WordNet";
	}
	public String getValue(AnnotatedToken t){
		if(t.getIndex().equals(0))	
			return "-NULL-";
		String value = "-NULL-";
		String token = t.getToken();
		char firstLetter = t.getPos().charAt(0);

		if(firstLetter == 'N'){
			value = getWordNetID(POS.NOUN, token);
		} 
		else if(firstLetter == 'V'){
			value = getWordNetID(POS.VERB, token);
		}
		else if(firstLetter == 'J'){
			value = getWordNetID(POS.ADJECTIVE, token);
		}
		else if (firstLetter == 'R'){
			value = getWordNetID(POS.ADVERB, token);
		}else{
			return "-NULL-";
		}		
		return value;
	}
	
	private String getWordNetID(POS wordNetPosTag, String token){
		
		IndexWord word = null;
		try {
			word = dict.lookupIndexWord(wordNetPosTag, token);
		} catch (JWNLException e) {
			e.printStackTrace();
			return "-NULL-";
		}
		if (word==null)
			return "-NULL-";
		Synset[] senses;
		try {
			senses = word.getSenses();
		} catch (JWNLException e) {
			e.printStackTrace();
			return "-NULL-";
		}

		String firstSenseKey = senses[0].getSenseKey(token);
		return firstSenseKey;
	}
	public static void configureJWordNet() {
		try {
			// initialize JWNL (this must be done before JWNL can be used)
			// See the JWordnet documentation for details on the properties file
			JWNL.initialize(new FileInputStream("data/WordNet/property/file_properties.xml"));
		} catch (Exception ex) {
			ex.printStackTrace();

			System.exit(-1);
		}
	}

}
