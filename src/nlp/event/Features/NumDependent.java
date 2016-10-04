package nlp.event.Features;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import nlp.annotator.pipeline.AnnotateAutomator;
import nlp.annotator.util.AnnotatedCorpus;
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.MyDependency;
import nlp.corpus.ACECorpus;
import nlp.event.Feature.CompoundFeature;
import nlp.event.Features.RelatedEntity.Order;

public class NumDependent extends CompoundFeature {
	

	public NumDependent(){
		
	}
	@Override
	public String getMineName() {
		//related entity names
		return "NumDependent";
		
	}

	/* (non-Javadoc)
	 * @see nlp.event.Feature.CompoundFeature#getname()
	 * NumDeTi: number of dependents of candidate word of type Time
	 * NumDeOrg: number of dependents of organization
	 * NumDeLoc: number of dependents of location
	 * LaDePer:Label of dependency relations to dependents of type person
	 * ConHePer:Constituent head words of dependent of type person
	 * NumEnPer: Number of entity mentions of type t reachable by some dependency path
	 * LenClPer: length of path to closest entity mention of type t
	 * 11 types: Time-Ti;Date-Da;Location-Loc;Percent-Pce;Orgnization-org;Person-Per;Money-Mon;Number-Num;
	 *           Duration-Dur;Ordinal-Ord.
	 */
	@Override
	public String getname() {
		// 50 feature names x:xx:xxx
		return "NumDeWea:NumDeLoc:NumDeCon:NumDeSen:NumDeNum:NumDeJob:NumDeVeh:NumDeO:NumDeOrg:NumDeGpe:NumDeFac:NumDePer:NumDeCri";
		
	}


	@Override
	public String getValue(AnnotatedToken t) {
		//System.out.println("candidate "+t.getToken());
		
		if(t.getIndex().equals(0))	return "-NULL-";
		
		AnnotatedSentence Sen = t.getParent();
		LinkedList<MyDependency> depList=Sen.getDeplist();
		
		String[] Ner={"wea","loc","contact-info","sentence","numeric","job-title","veh","o","org","gpe","fac","per","crime"};
		int[] num=new int[Ner.length];
		for (int j=0;j<Ner.length;j++){
			for (int i=0;i<depList.size();i++){
				MyDependency md=depList.get(i);
				AnnotatedToken head=md.getHead();
				AnnotatedToken dependent=md.getDependent();
				if (!head.getToken().equals("ROOT") && head.equals(t) && md.getType()!= null){
					//System.out.println(dependent.getTokenNE());
					if (dependent.getTokenNE().toLowerCase().equals(Ner[j])) {
						num[j]++;
						
					}
				}
				
			}
		
		}
		
		
		String numI="";
		for (int i=0;i<num.length-1;i++){
			numI+=String.valueOf(num[i])+":";
			
		}
		numI+=String.valueOf(num[num.length-1]);
		
		return numI;
	}
	/*
	public static void main(String[] args){
		AnnotateAutomator aAutomator = null;
		try {
			aAutomator = new AnnotateAutomator(true);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnnotatedCorpus acorp = new AnnotatedCorpus();
		try {
			acorp.read(new File("./data/AnnotatedFile/"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		acorp.getDoclist().get(0);
		AnnotatedDoc aDoc = acorp.getDoclist().get(0);
		Iterator<AnnotatedSentence> it = aDoc.iterator();
		while(it.hasNext()){
			AnnotatedSentence sen = it.next();
			Iterator<AnnotatedToken> itt=sen.iterator();
			itt.next();
			while(itt.hasNext()){
				AnnotatedToken toke=itt.next();
				NumDependent fw = new NumDependent();
				String tmp=fw.getValue(toke);
				System.out.println(tmp);
			}
			//LinkedList<MyDependency> depList=sen.getDeplist();
			//System.out.println(depList.toString());
			
			
		}	
		
		 
	}*/
	

}

