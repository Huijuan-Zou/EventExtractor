package nlp.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import nlp.model.util.UnixScript;

public class Timbl implements Model{
	UnixScript exe;
	ArrayList<String> traincommand; ;
	ArrayList<String> testcommand;
	private final Logger log = Logger.getLogger(Timbl.class.getName());
	private Integer tp1,tp0,fp,tn,fn1,fn0;
	Hashtable<String,Integer> pFlags;
	public Timbl(String[] p) throws IOException {
		this.pFlags = new Hashtable<String,Integer>();
		for(int i = 0; i < p.length; i++)
			pFlags.put(p[i], 1);
		exe = new UnixScript("/Users/huijuanzou/git/lamachine/bin/");
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
	@Override
	public void train(String trainfile, String modelpath) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> temp = new ArrayList<String>(traincommand);
		temp.add("-f");temp.add(trainfile);
		temp.add("-I");temp.add(modelpath);
		
		exe.execute(temp);
		
		log.info(exe.getStdout());
		log.info(exe.getStderr());
	}

	@Override
	public void predict(String modelpath, String testfile) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> temp = new ArrayList<String>(testcommand);
		temp.add("-i");temp.add(modelpath);
		temp.add("-t");temp.add(testfile);
		
		exe.execute(temp);
		String stdout = exe.getStdout();
		log.info(stdout);
		log.info(exe.getStderr());
		stdout = stdout.substring(stdout.lastIndexOf("Writing output in:")+18);
		String outpath = stdout.substring(0, stdout.indexOf('\n')).trim();
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
		File f = new File(outpath);
		f.renameTo(new File(outpath.substring(0, outpath.lastIndexOf("/")+1)+"out"));
		log.info("Recall:"+this.getRecall());
		log.info("Precision:"+this.getPrecision());
		log.info("Accuracy:"+this.getAccuracy());
		log.info("F1:"+this.getFvalue(1));
	}

	@Override
	public Float getRecall() {
		return new Float(tp1)/(tp1+tp0+fp);
	}

	@Override
	public Float getPrecision() {
		return new Float(tp1)/(tp1+tp0+tn);
	}

	@Override
	public Float getAccuracy() {
		return new Float(tp1+fn1)/(tp1+tp0+tn+fp+fn1+fn0);
	}
	@Override
	public Float getFvalue(int b) {
		Float p = new Float(tp1)/(tp1+tp0+tn);
		Float r = new Float(tp1)/(tp1+tp0+fp);;
		return (b*b+1)*p*r/(b*b*p+r);
	}
}
