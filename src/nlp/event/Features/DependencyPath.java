package nlp.event.Features;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.MyDependency;
import nlp.annotator.util.entity.EntityMention;
import nlp.event.Feature.PairFeature;

public class DependencyPath implements PairFeature {
	private LinkedList<AnnotatedToken> tokenList;
	private HashMap <AnnotatedToken,LinkedList<AnnotatedToken>> neighborList;
	private HashMap <AnnotatedToken,LinkedList<String>> typeList;
	private LinkedList<MyDependency> depList;
	private LinkedList<String> labelList;
	private LinkedList<String> wordList;
	private LinkedList<String> posList;

	@Override
	public String getname() {
		// TODO Auto-generated method stub
		return "DependencyPath";
	}
	/**
	 * get tokenList (store all vertex for the graph)
	 */
	private void getTokenList(){
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
	private void getNeighbor(){
		 neighborList=new HashMap <AnnotatedToken,LinkedList<AnnotatedToken>> ();
		 typeList=new HashMap<AnnotatedToken,LinkedList<String>>();
		for (int i=0;i<tokenList.size();i++){
			AnnotatedToken t=tokenList.get(i);
			LinkedList<AnnotatedToken> tToke=new LinkedList<AnnotatedToken>();
			LinkedList<String> tType=new LinkedList<String>();
			for (int j=0;j<depList.size();j++){
				MyDependency md=depList.get(j);
				//System.out.println(md);
				AnnotatedToken head=md.getHead();
				AnnotatedToken dependent=md.getDependent();
				String type=md.getType();
				//System.out.println(head.getTokenNE());
				//System.out.println(dependent.getTokenNE());
				if (!head.getToken().equals("ROOT")&&head.equals(t)&& !tToke.contains(dependent)){
					tToke.add(dependent);
					tType.add(type);

				}
				if (!head.getToken().equals("ROOT")&&dependent.equals(t)&& !tToke.contains(head)){
					tToke.add(head);
					tType.add(type);
					
				}
				
			}
			if (tToke.isEmpty()) {
				tToke.add(new AnnotatedToken("-NULL-"));
				tType.add("-NULL-");
				}
			neighborList.put(t, tToke);
			typeList.put(t,tType);
		}
		
		
	}
	private HashMap<AnnotatedToken, Boolean> initialVisited(LinkedList<AnnotatedToken> tokenList){
		HashMap<AnnotatedToken, Boolean> visited=new HashMap<AnnotatedToken, Boolean>();
		for (int i=0;i<tokenList.size();i++){
			visited.put(tokenList.get(i),false);
		}
		
		return visited;
	}
	private void bfs(AnnotatedToken t, AnnotatedToken cHead){
		HashMap<AnnotatedToken, Boolean> visited=initialVisited(tokenList);
		Queue<AnnotatedToken> q=new LinkedList<AnnotatedToken>();
		labelList=new LinkedList<String>();
		wordList=new LinkedList<String>();
		posList=new LinkedList<String>();
		
		
		wordList.add(t.getToken());
		posList.add(t.getTokenNE());
		if (!t.equals(cHead)) {
			q.add(t);
			visited.put(t,true);
			}
		
		
		while(!q.isEmpty()){
			AnnotatedToken tok=q.poll();
			LinkedList<AnnotatedToken> nb = neighborList.get(tok);
			for (int i=0;i<nb.size();i++){
				AnnotatedToken nowNb = nb.get(i);
				
				if(!visited.get(nowNb)){
					visited.put(nowNb, true);
					q.add(nowNb);
					
				}
				else{
					
				}
				
				if (nowNb.equals(cHead)) break;
			}
		}
		
	}

	@Override
	public String getValue(AnnotatedToken t, EntityMention em) {
		if(t.getIndex().equals(0))	return "-NULL-";
		//AnnotatedToken cHead=em.getCHead();
		AnnotatedSentence Sen = t.getParent();
		depList=Sen.getDeplist();
		getTokenList();
		getNeighbor();
		//bfs(t, cHead);
		
		
		
		return null;
	}

}
