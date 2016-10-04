package nlp.event.Features;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.event.Feature.Feature;
import nlp.util.Pair;

public class IsAnchorIdentifier extends Feature {

	public IsAnchorIdentifier() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getname() {
		return "IsAnchorIdentifier";
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
			if(off.getFirst().compareTo(start)>=0 && off.getSecond().compareTo(end)<=0)
				return "yes";
		}
		return "no";
	}
}
