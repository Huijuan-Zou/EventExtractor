package nlp.event.instance;

import java.util.LinkedList;

import nlp.util.Pair;

public class InstanceToken extends Instance{
	
	public InstanceToken(String token, LinkedList<Pair<String,String>> featuresList, String label){
		super(featuresList,token,label);	
		// TODO Auto-generated constructor stub
	}
	public InstanceToken(String token, LinkedList<Pair<String,String>> featuresList){
		super(featuresList,token);
	}
}
