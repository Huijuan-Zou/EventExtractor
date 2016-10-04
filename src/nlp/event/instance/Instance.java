package nlp.event.instance;

import java.util.LinkedList;

import nlp.util.Pair;

public class Instance {
	protected final LinkedList<Pair<String,String>> featuresList;
	protected final String label;
	protected final String value;
	public Instance(LinkedList<Pair<String,String>> featuresList, String value, String label){
		this.label = label;
		this.value = value;
		this.featuresList = featuresList;
	}
	public Instance(LinkedList<Pair<String,String>> featuresList,String value){
		this.label = null;
		this.value = value;
		this.featuresList = featuresList;
	}
	public LinkedList<Pair<String,String>> getFeaturesList() {
		return featuresList;
	}
	public String getLabel() {
		if(label!=null)
			return label;
		return null;
	}
	public String getValue(){
		return this.value;
	}
}
