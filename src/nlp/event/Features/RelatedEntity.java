package nlp.event.Features;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.MyDependency;
import nlp.event.Feature.CompoundFeature;
import nlp.event.Features.RelatedEntity.Order;

public class RelatedEntity extends CompoundFeature {
	private HashMap<String,Integer> shortestPath;
	private HashMap<String, Integer> typeCount;
	private LinkedList<AnnotatedToken> tokenList;
	private HashMap <AnnotatedToken,LinkedList<AnnotatedToken>> neighborList;
	

	public RelatedEntity(){
		
	}
	@Override
	public String getMineName() {
		//related entity names
		return "RelatedEntity";
		
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
		return "NumDeWea:NumDeLoc:NumDeCon:NumDeSen:NumDeNum:NumDeJob:NumDeVeh:NumDeO:NumDeOrg:NumDeGpe:NumDeFac:NumDePer:NumDeCri:LaDeWea:LaDeLoc:LaDeCon:LaDeSen:LaDeNum:LaDeJob:LaDeVeh:LaDeO:LaDeOrg:LaDeGpe:LaDeFac:LaDePer:LaDeCri:ConHeWea:ConHeLoc:ConHeCon:ConHeSen:ConHeNum:ConHeJob:ConHeVeh:ConHeO:ConHeOrg:ConHeGpe:ConHeFac:ConHePer:ConHeCri:NumEnWea:NumEnLoc:NumEnCon:NumEnSen:NumEnNum:NumEnJob:NumEnVeh:NumEnO:NumEnOrg:NumEnGpe:NumEnFac:NumEnPer:NumEnCri:LenClWea:LenClLoc:LenClCon:LenClSen:LenClNum:LenClJob:LenClVeh:LenClO:LenClOrg:LenClGpe:LenClFac:LenClPer:LenClCri";
		
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
	
	private HashMap<AnnotatedToken, Boolean> initialVisited(LinkedList<AnnotatedToken> tokenList){
		HashMap<AnnotatedToken, Boolean> visited=new HashMap<AnnotatedToken, Boolean>();
		for (int i=0;i<tokenList.size();i++){
			visited.put(tokenList.get(i),false);
		}
		
		return visited;
	}
	
	private void bfs(AnnotatedToken t,String[] Ner){
		
		HashMap<AnnotatedToken, Boolean> visited=initialVisited(tokenList);
		Queue<AnnotatedToken> q=new LinkedList<AnnotatedToken>();
		typeCount=new HashMap<String, Integer>();
		shortestPath=new HashMap<String,Integer>();
		Hashtable<AnnotatedToken,Integer> tokenPath=new Hashtable<AnnotatedToken, Integer>();
		for (int i=0;i<Ner.length;i++){
			typeCount.put(Ner[i], 0);
		}
		
		q.add(t);
		visited.put(t,true);
		typeCount.put(t.getTokenNE().toLowerCase(),typeCount.get(t.getTokenNE().toLowerCase())+1);
		
		tokenPath.put(t,0);
		
		while(!q.isEmpty()){
			AnnotatedToken tok=q.poll();
			LinkedList<AnnotatedToken> nb = neighborList.get(tok);
			for (int i=0;i<nb.size();i++){
				AnnotatedToken nowNb = nb.get(i);
				int curPath=tokenPath.get(tok);
				if(!visited.get(nowNb)){
					visited.put(nowNb, true);
					String ner=nowNb.getTokenNE().toLowerCase();
					typeCount.put(ner,typeCount.get(ner)+1);
					tokenPath.put(nowNb,curPath+1);
					q.add(nowNb);
				}
				else{
					if (curPath+1<tokenPath.get(nowNb)){
						tokenPath.put(nowNb,curPath+1);
					}
				}
			}
			
		}
		
		for (int i=0;i<tokenList.size();i++){
			AnnotatedToken tok = null;
			
			String ner = null ;
			Integer shortestNum = 0;
			
				tok = tokenList.get(i);
				ner = tok.getTokenNE().toLowerCase();
				shortestNum=tokenPath.get(tok);
			if(shortestNum==null)
				shortestNum = Integer.MAX_VALUE;
			
			if (!shortestPath.containsKey(ner)){
				shortestPath.put(ner, shortestNum);
				
			}
			else if(shortestPath.get(ner).compareTo(shortestNum)>0){
				shortestPath.put(ner,shortestNum);
			}	
		}
		
		
	}
	/**
	 * get tokenList (store all vertex for the graph)
	 */
	private void getTokenList(LinkedList<MyDependency> depList){
		tokenList=new LinkedList<AnnotatedToken>();
		for (int i=0;i<depList.size();i++){
			MyDependency md=depList.get(i);
			AnnotatedToken t1=md.getHead();
			AnnotatedToken t2=md.getDependent();
			
			if (!t1.getToken().equals("ROOT")&&!tokenList.contains(t1)){
				tokenList.add(t1);
				
			}
			if (!t1.getToken().equals("ROOT")&&!tokenList.contains(t2)){
				tokenList.add(t2);
				
			}
			
			
		}
		
	}
	/**
	 * get neighbor list for all tokens
	 */
	
	private void getNeighbor(LinkedList<MyDependency> depList){
		 neighborList=new HashMap <AnnotatedToken,LinkedList<AnnotatedToken>> ();
		for (int i=0;i<tokenList.size();i++){
			AnnotatedToken t=tokenList.get(i);
			LinkedList<AnnotatedToken> tToke=new LinkedList<AnnotatedToken>();
			for (int j=0;j<depList.size();j++){
				MyDependency md=depList.get(j);
				//System.out.println(md);
				AnnotatedToken head=md.getHead();
				AnnotatedToken dependent=md.getDependent();
				//System.out.println(head.getTokenNE());
				//System.out.println(dependent.getTokenNE());
				if (!head.getToken().equals("ROOT")&&head.equals(t)&& !tToke.contains(dependent)){
					tToke.add(dependent);
					

				}
				if (!head.getToken().equals("ROOT")&&dependent.equals(t)&& !tToke.contains(head)){
					tToke.add(head);
					
				}
				
			}
			if (tToke.isEmpty()) tToke.add(new AnnotatedToken("-NULL-"));
			neighborList.put(t, tToke);
		}
		
		
	}
	
	private void getPath(LinkedList<MyDependency> depList,String[] Ner,AnnotatedToken t){
		
		getTokenList(depList);
		if (!tokenList.contains(t)) {
			typeCount=new HashMap<String, Integer>();
			shortestPath=new HashMap<String,Integer>();
			for (int i=0;i<Ner.length;i++){
				typeCount.put(Ner[i], 0);
			
			}
		}
		else{
			getNeighbor(depList);
			bfs(t, Ner);
		}
		
		
	}
	@Override
	public String getValue(AnnotatedToken t) {
		//System.out.println("candidate "+t.getToken());
		
		if(t.getIndex().equals(0))	return "-NULL-";
		
		AnnotatedSentence Sen = t.getParent();
		LinkedList<MyDependency> depList=Sen.getDeplist();
		
		LinkedList<PriorityQueue<Order>> LaDe=new LinkedList<PriorityQueue<Order>>();
		LinkedList<PriorityQueue<Order>> ConHe=new LinkedList<PriorityQueue<Order>>();
		
		String[] Ner={"wea","loc","contact-info","sentence","numeric","job-title","veh","o","org","gpe","fac","per","crime"};
		int[] num=new int[Ner.length];
		for (int j=0;j<Ner.length;j++){
			PriorityQueue<Order> t1=new PriorityQueue<Order>();
			PriorityQueue<Order> t2=new PriorityQueue<Order>();
			for (int i=0;i<depList.size();i++){
				MyDependency md=depList.get(i);
			
				if (!md.getHead().getToken().equals("ROOT") && md.getHead().equals(t) && md.getType()!= null){
					if (md.getDependent().getTokenNE().toLowerCase().equals(Ner[j])) {
						num[j]++;
						t1.add(new Order(md.getType(),md.getDependent().getIndex()));
						t2.add(new Order(md.getDependent().getToken(),md.getDependent().getIndex()));
					}
				}
				
			}
			if (t1.isEmpty()) t1.add(new Order("-NULL-", 0));
			if(t2.isEmpty()) t2.add(new Order("-NULL-", 0));
			LaDe.add(t1);
			ConHe.add(t2);
		}
		
		
		getPath(depList,Ner,t);
		
		String numI="";
		for (int i=0;i<num.length;i++){
			numI+=String.valueOf(num[i])+":";
			
		}
		
		String laD="";
		for (int i=0;i<LaDe.size();i++){
			PriorityQueue<Order> t1=LaDe.get(i);
			String tmp="";
			Iterator<Order> it=t1.iterator();
			while(it.hasNext()){
				Order m=it.next();
				tmp+=m.getObj()+"*";
			}
			tmp=tmp.substring(0,tmp.length()-1);
			laD+=tmp+":";
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
		
		String tCount="";
		String shortPath="";
		
		for (int i=0;i<Ner.length;i++){
			String tmp1=String.valueOf(typeCount.get(Ner[i]));
			String tmp2=String.valueOf(shortestPath.get(Ner[i]));
			tCount+=tmp1+":";
			if (i<Ner.length-1)
				shortPath+=tmp2+":";
			else shortPath+=tmp2;
		}
		String compFeature=numI+laD+conH+tCount+shortPath;
		
		return compFeature;
	}

}
