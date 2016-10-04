package nlp.event.Features;
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
import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.MyDependency;
import nlp.corpus.ACECorpus;
import nlp.event.Feature.CompoundFeature;
import nlp.event.Features.RelatedEntity.Order;

public class ConstituteHead extends CompoundFeature {
	
	

	public ConstituteHead(){
		
	}
	@Override
	public String getMineName() {
		//related entity names
		return "ConstituteHead";
		
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
		return "ConHeWea:ConHeLoc:ConHeCon:ConHeSen:ConHeNum:ConHeJob:ConHeVeh:ConHeO:ConHeOrg:ConHeGpe:ConHeFac:ConHePer:ConHeCri";
		
	}

	class Order implements Comparable<Order>{
		private String obj;
		private int index;
		public Order(String a, int b){
			this.obj=a;
			this.index=b;
		}
		public String getObj(){
			return this.obj;
		}
		public int getIndex(){
			return this.index;
		}
		@Override
		public int compareTo(Order o) {
			return this.getIndex() - o.getIndex();
		}
	}
	
	
	
	
	@Override
	public String getValue(AnnotatedToken t) {
		//System.out.println("candidate "+t.getToken());
		
		if(t.getIndex().equals(0))	return "-NULL-";
		
		AnnotatedSentence Sen = t.getParent();
		LinkedList<MyDependency> depList=Sen.getDeplist();
		
		
		LinkedList<PriorityQueue<Order>> ConHe=new LinkedList<PriorityQueue<Order>>();
		
		String[] Ner={"wea","loc","contact-info","sentence","numeric","job-title","veh","o","org","gpe","fac","per","crime"};
		
		for (int j=0;j<Ner.length;j++){
			
			PriorityQueue<Order> t2=new PriorityQueue<Order>();
			for (int i=0;i<depList.size();i++){
				MyDependency md=depList.get(i);
				AnnotatedToken head=md.getHead();
				AnnotatedToken dependent=md.getDependent();
				if (!head.getToken().equals("ROOT") && head.equals(t) && md.getType()!= null&& dependent.getTokenNE()!= null){
					if (dependent.getTokenNE().toLowerCase().equals(Ner[j])) {
						
						t2.add(new Order(dependent.getToken(),dependent.getIndex()));
					}
				}
				
			}
			
			if(t2.isEmpty()) t2.add(new Order("-NULL-", 0));
		
			ConHe.add(t2);
		}
		
		
	
		
		
		
		String conH="";
		for (int i=0;i<ConHe.size();i++){
			PriorityQueue<Order> t1=ConHe.get(i);
			String tmp="";
			Iterator<Order> it=t1.iterator();
			while(it.hasNext()){
				Order m=it.next();
				tmp+=m.getObj()+"*";
			}
			tmp=tmp.substring(0,tmp.length()-1);
			conH+=tmp+":";
		}
		
		conH=conH.substring(0,conH.length()-1);
		
		return conH;
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
		aAutomator.setCorpus(new ACECorpus("./data/ACE/bc/1"));
		try {
			aAutomator.annotate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnnotatedDoc aDoc = aAutomator.getAnnotatedDoc();
		Iterator<AnnotatedSentence> it = aDoc.iterator();
		while(it.hasNext()){
			AnnotatedSentence sen = it.next();
			Iterator<AnnotatedToken> itt=sen.iterator();
			itt.next();
			while(itt.hasNext()){
				AnnotatedToken toke=itt.next();
				ConstituteHead fw = new ConstituteHead();
				String tmp=fw.getValue(toke);
				System.out.println(tmp);
			}
			//LinkedList<MyDependency> depList=sen.getDeplist();
			//System.out.println(depList.toString());
			
			
		}	
		
		 
	}*/

}

