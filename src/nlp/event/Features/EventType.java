package nlp.event.Features;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;
import nlp.util.Pair;

/**
 * @author Robin
 *	eventtype-subtype
 *v
 */
public class EventType extends Feature {

	static final public String[] pValues = {
			"Life*Be-Born","Life*Die","Life*Divorce","Life*Injure","Life*Marry",
			"Business*Declare-Bankruptcy","Business*Merge-Org","Business*End-Org","Business*Start-Org",
			"Personnel*Elect","Personnel*Start-Position","Personnel*Nominate","Personnel*End-Position",
			"Contact*Phone-Write","Contact*Meet",
			"Transaction*Transfer-Ownership","Transaction*Transfer-Money",
			"Justice*Convict","Justice*Extradite","Justice*Sentence","Justice*Arrest-Jail","Justice*Release-Parole","Justice*Acquit",
			"Justice*Trial-Hearing","Justice*Execute","Justice*Sue","Justice*Fine","Justice*Appeal","Justice*Charge-Indict","Justice*Pardon",
			"Conflict*Attack","Conflict*Demonstrate",
			"Movement*Transport"};
	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "EventType";
	}
	@Override
	public String getValue(AnnotatedToken t) {
		if(t.getIndex().equals(0))	return "-NULL-";

		Document meta = t.getParent().getParent().getMetadata();
		Elements anchors = meta.getElementsByTag("anchor");
		int start = 0;
		int end = 0;
		Pair<Integer,Integer> off = t.getOffset();
		for(Element anchor : anchors){
			Element seq = anchor.getElementsByTag("charseq").first();
			start = Integer.valueOf(seq.attr("START"));
			end = Integer.valueOf(seq.attr("END"));
			if(off.getFirst().compareTo(start)>=0 && off.getSecond().compareTo(end)<=0){
				Element event = anchor.parent().parent();
				return event.attr("TYPE")+'*'+event.attr("SUBTYPE");
			}
		}
		return "NOTEVENT";
	}

}
