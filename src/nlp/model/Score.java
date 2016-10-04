package nlp.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import nlp.event.Features.EventType;
import nlp.model.util.UnixScript;

public class Score{
	UnixScript exe;
	ArrayList<String> traincommand; ;
	ArrayList<String> testcommand;
	private final Logger log = Logger.getLogger(Timbl.class.getName());
	private Integer tp1,tp0,fp,tn,fn1,fn0;
	Hashtable<String,Integer> pFlags;
	public Score(String[] p) throws IOException {
		this.pFlags = new Hashtable<String,Integer>();
		for(int i = 0; i < p.length; i++)
			pFlags.put(p[i], 1);
		exe = new UnixScript("/Users/Robin/lamachine/bin/");
		traincommand = new ArrayList<String>();
		traincommand.add("timbl");traincommand.add("-m");traincommand.add("O");
		traincommand.add("-d");traincommand.add("ID");
		traincommand.add("-k");traincommand.add("5");
		
		testcommand = new ArrayList<String>();
		testcommand.add("timbl");
	}
	public void setPFlags(String[] p){
		pFlags = new Hashtable<String,Integer>();
		for(int i = 0; i < p.length; i++)
			pFlags.put(p[i], 1);
	}

	public void predict(String outpath) throws IOException {
		tp1 = 0; tp0 = 0; fp = 0; tn = 0; fn1 = 0; fn0 = 0;
		BufferedReader br =	new BufferedReader(new FileReader(new File(outpath)));
		String line = "";
		while((line = br.readLine())!=null){
			String[] ts = line.split(",");
			String prediction = ts[ts.length-2];
			String label = ts[ts.length-1];
			if(prediction.equals(label)){
				if(pFlags.get(label)!=null)
					tp1 ++;
				else
					fn1 ++;
			}
			else{
				if(pFlags.get(prediction)!=null){
					if( pFlags.get(label)!=null)
						tp0 ++;
					else
						tn++;
					}
				else{
					if( pFlags.get(label)!=null)
						fp ++;
					else
						fn0 ++;
					}
			}
		}
		br.close();
		System.out.println("Recall:"+this.getRecall());
		System.out.println("Precision:"+this.getPrecision());
		System.out.println("Accuracy:"+this.getAccuracy());
		System.out.println("F1:"+this.getFvalue(1));
	}

	public Float getRecall() {
		return new Float(tp1)/(tp1+tp0+fp);
	}
	public Float getPrecision() {
		return new Float(tp1)/(tp1+tp0+tn);
	}
	public Float getAccuracy() {
		return new Float(tp1+fn1)/(tp1+tp0+tn+fp+fn1+fn0);
	}
	public Float getFvalue(int b) {
		Float p = new Float(tp1)/(tp1+tp0+tn);
		Float r = new Float(tp1)/(tp1+tp0+fp);;
		return (b*b+1)*p*r/(b*b*p+r);
	}
	public static void main(String[] args) throws IOException{
		Score s =new Score(EventType.pValues);
		s.predict("./data/1/annotatedCorpus_test.IB1.O.gr.k1.out");
	}
}
