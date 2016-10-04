package nlp.event.extractor;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import nlp.event.Feature.FeatureBuilder;
import nlp.model.Model;
import nlp.model.Timbl;

public class AnchorIdentifier {
	private Model bmodel;
	private Model cmodel;
	private Boolean splitMode;
	private File m_s;
	private final Logger log = Logger.getLogger(AnchorIdentifier.class.getName());
	public AnchorIdentifier(Model m,Model m1) {
		bmodel = m;
		cmodel = m1;
		this.splitMode = true;
		m_s = new File("./data/model/");
		m_s.mkdirs();
	}
	public AnchorIdentifier(Model m1) {
		bmodel = null;
		cmodel = m1;
		this.splitMode = false;
		m_s = new File("./data/model/");
		m_s.mkdirs();
	}
	/** set anchoridentifier working mode, true as in split mode false as in integrated mode
	 * @param m
	 * @return
	 */
	public Boolean setSplitMode(Boolean m){
		this.splitMode = m;
		return this.splitMode;
	}
	public void setTrainingCorpus(){
		
	}
	public void setTestCorpus(){
		
	}
	public void setCModel(Model m){
		this.cmodel = m;
	}
	/** Set training model
	 * @param m
	 */
	public void setBModel(Model m){
		this.bmodel = m;
	}
	/** binary classifier training
	 * @param train
	 */
	private void trainB(String train){
		log.info("Starting B-Training.");
		try {
			bmodel.train(train,m_s.toString()+"/bmodel");
			log.info("B-Training Finished.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.error("Error occured in B-training:");
			StackTraceElement[] es = e.getStackTrace();
			String stacktrace = "";
			for(int i = 0; i < es.length; i++){
				stacktrace += (es[i].toString()+'\n');
			}
			log.error(stacktrace);
		}
	}
	/** binary classifier prediction
	 * @param f testfile path
	 */
	private void predictB(String f){
		log.info("Starting B-Prediction.");
		try {
			bmodel.predict(m_s.toString()+"/bmodel",f);
			log.info("B-Prediction Finished.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Error occured in B-Prediction:");
			StackTraceElement[] es = e.getStackTrace();
			String stacktrace = "";
			for(int i = 0; i < es.length; i++){
				stacktrace += (es[i].toString()+'\n');
			}
			log.error(stacktrace);
		}
	}
	
	/** Multi-classifier training
	 * @param train
	 */
	private void trainM(String train){
		log.info("Starting M-Training.");
		try {
			cmodel.train(train,m_s.toString()+"/mmodel");
			log.info("M-Training Finished.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.error("Error occured in M-training:");
			StackTraceElement[] es = e.getStackTrace();
			String stacktrace = "";
			for(int i = 0; i < es.length; i++){
				stacktrace += (es[i].toString()+'\n');
			}
			log.error(stacktrace);
		}
	}
	/** Multi-classifier prediction
	 * @param f testfile path
	 */
	private void predictM(String f){
		log.info("Starting M-Prediction.");
		try {
			cmodel.predict(m_s.toString()+"/mmodel",f);
			log.info("M-Prediction Finished.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Error occured in M-Prediction:");
			StackTraceElement[] es = e.getStackTrace();
			String stacktrace = "";
			for(int i = 0; i < es.length; i++){
				stacktrace += (es[i].toString()+'\n');
			}
			log.error(stacktrace);
		}
	}
	
	public void train(String train){
		if(!this.splitMode)
			this.trainM(train);
	}
	public void predict(String f){
		if(!this.splitMode)
			this.predictM(f);
	}
	static void main(String[] args) throws IOException{
		PropertyConfigurator.configure("log4j.properties");
		String[] f = {"yes"};
		AnchorIdentifier ai = new AnchorIdentifier(new Timbl(f),new Timbl(f));
		ai.train("data/annotatedCorpus_train");
		ai.predict("data/annotatedCorpus_test");
	}
}
