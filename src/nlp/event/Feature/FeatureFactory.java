package nlp.event.Feature;

import nlp.event.Features.*;

public class FeatureFactory{

	public FeatureFactory() {
		// TODO Auto-generated constructor stub
	}
	public Feature newFeature(String des){
		if(des.equals("DependentType")) return new DependentType();
		else if(des.equals("DependencyType")) return new DependencyType();
		else if(des.equals("EventType")) return new EventType();
		else if(des.equals("FullWord")) return new FullWord();
		else if(des.equals("FakeEventType")) return new FakeEventType();
		else if(des.equals("HeadPos"))	return new HeadPos();
		else if(des.equals("HeadType")) return new HeadType();
		else if(des.equals("HeadWord")) return new HeadWord();
		else if(des.equals("IsAnchorIdentifier")) return new IsAnchorIdentifier();
		else if(des.equals("LcWord")) return new LcWord();
		else if(des.equals("LemmaWord")) return new LemmaWord();
		else if(des.equals("NEntityType")) return new NEntityType();
		else if(des.equals("Offset")) return new Offset();
		else if(des.equals("PosTag")) return new PosTag();
		else if(des.equals("WordNet")) return new WordNet();
		else if(des.equals("WordDepth")) return new WordDepth();
		return null;
	}
	public CompoundFeature newComFeature(String des){
		if(des.equals("LeftContextLowCase")) return new LeftContextLowCase();
		else if(des.equals("LeftContextPosTag")) return new LeftContextPosTag();
		else if(des.equals("RightContextLowCase")) return new RightContextLowCase();
		else if(des.equals("RightContextPosTag")) return new RightContextPosTag();
		else if(des.equals("RelatedEntity")) return new RelatedEntity();
		else if(des.equals("NumDependent")) return new NumDependent();
		else if(des.equals("LabelDependent")) return new LabelDependent();
		else if(des.equals("ConstituteHead")) return new ConstituteHead();
		else if(des.equals("NumTypeT")) return new NumTypeT();
		else if(des.equals("LenPath")) return new LenPath();
		return null;
	}
	public PairFeature newPairFeature(String des){
		return null;
	}
}
