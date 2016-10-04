package nlp.event.Features;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.entity.EntityMention;
import nlp.event.Feature.PairFeature;
import nlp.util.Pair;

/**
 * @author Robin
 *	role
 */
public class IsArgument implements PairFeature {

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "IsArgument";
	}
	private Boolean within(int begin, int end, AnnotatedToken e){
		return ( e.getOffset().getFirst().compareTo(begin) >= 0 ) && 
				( e.getOffset().getSecond().compareTo(end) <= 0);
	}
	
	public String getValue(AnnotatedToken t, AnnotatedToken e) {
		if(t.getIndex().equals(0))	return "-NULL-";

		Document meta = t.getParent().getParent().getMetadata();
		Elements anchors = meta.getElementsByTag("anchor");

		Pair<Integer,Integer> off = t.getOffset();
		for(Element anchor : anchors){
			Element seq = anchor.getElementsByTag("charseq").first();
			if(within(Integer.valueOf(seq.attr("START")),Integer.valueOf(seq.attr("END")),t)){
				Element event = anchor.parent().parent();
				for(Element event_arg : event.getElementsByTag("event_argument")){
					//check if this argument matched my entity
					Element entity = meta.getElementById(event_arg.attr("REFID"));
					for(Element seq1 :entity.getElementsByTag("charseq")){
						if(within(Integer.valueOf(seq1.attr("START")),Integer.valueOf(seq1.attr("END")),e)){
							event_arg.attr("ROLE");
						}
					}
				}
			}
		}
		return "NO";
	}
	@Override
	public String getValue(AnnotatedToken t, EntityMention em) {
		// TODO Auto-generated method stub
		return null;
	}

}
