package nlp.event.Feature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import nlp.annotator.util.AnnotatedDoc;
import nlp.annotator.util.AnnotatedSentence;
import nlp.annotator.util.AnnotatedToken;
import nlp.annotator.util.entity.EntityMention;
import nlp.corpus.document.format.Format;
import nlp.corpus.document.format.TimblFormat;
import nlp.event.instance.Instance;
import nlp.event.instance.InstanceToken;
import nlp.util.Pair;

public class FeatureBuilder {
	Feature[] fs;
	CompoundFeature[] cfs;
	PairFeature[] pfs;
	Feature label;
	PairFeature pairlabel;
	Format format;
	Document metadata;
	private final Logger log = Logger.getLogger(FeatureBuilder.class.getName());
	public FeatureBuilder(String[] featureset,String label,Format format) {
		this.format = format;
		this.fs = new Feature[featureset.length];
		FeatureFactory ff = new FeatureFactory();
		for(int i = 0; i < featureset.length; i++){
			fs[i] = ff.newFeature(featureset[i]);
		}
		cfs = null;
		pfs = null;
		this.label = ff.newFeature(label);
		this.pairlabel = null;
	}
	public FeatureBuilder(String[] featureset, String[] comfs,String label,Format format) {
		this.format = format;
		this.fs = new Feature[featureset.length];
		FeatureFactory ff = new FeatureFactory();
		for(int i = 0; i < featureset.length; i++){
			fs[i] = ff.newFeature(featureset[i]);
		}
		cfs = new CompoundFeature[comfs.length];
		for(int i = 0; i < comfs.length; i++){
			cfs[i] = ff.newComFeature(comfs[i]);
		}
		pfs = null;
		this.label = ff.newFeature(label);
		this.pairlabel = null;
	}
	public FeatureBuilder(String[] featureset, String[] comfs, String[] pairfs, PairFeature pairFeature,
			TimblFormat timblFormat) {
		this.format = format;
		FeatureFactory ff = new FeatureFactory();
		this.fs = new Feature[featureset.length];
		for(int i = 0; i < featureset.length; i++){
			fs[i] = ff.newFeature(featureset[i]);
		}
		cfs = new CompoundFeature[comfs.length];
		for(int i = 0; i < comfs.length; i++){
			cfs[i] = ff.newComFeature(comfs[i]);
		}
		pfs = new PairFeature[pairfs.length];
		for(int i = 0; i < pairfs.length; i++){
			pfs[i] = ff.newPairFeature(pairfs[i]);
		}
		this.pairlabel = pairFeature;
		this.label = null;
	}

	public Instance buildfeature(AnnotatedToken t,Boolean addlabel){
		LinkedList<Pair<String,String>> tline = new LinkedList<Pair<String,String>>();
		for(int i = 0; i < fs.length; i++){
			tline.add(new Pair<String,String>(fs[i].getname(),fs[i].getValue(t)));
		}
		for(CompoundFeature cf:this.cfs){
			String[] names = cf.getname().split(":");
			String[] values = cf.getValue(t).split(":");
			for(int i = 0; i < names.length; i++)
				tline.add(new Pair<String,String>(names[i],values[i]));
		}
		InstanceToken iToken;
		if(addlabel)
			iToken = new InstanceToken(t.getToken(),tline,label.getValue(t));
		else
			iToken = new InstanceToken(t.getToken(),tline);
		return iToken;
	}
	public LinkedList<Instance> buildfeature(AnnotatedSentence aSen,Boolean addlabel){
		LinkedList<Instance> iList = new LinkedList<Instance>();
		Iterator<AnnotatedToken> it = aSen.iterator();
		it.next();//skip the first root token
		while(it.hasNext()){
			iList.add(this.buildfeature(it.next(),addlabel));
		}
		return iList;
	}
	public Instance buildfeature(AnnotatedToken aToken,EntityMention eM,Boolean addlabel){
		LinkedList<Pair<String,String>> tline = new LinkedList<Pair<String,String>>();
		for(int i = 0; i < pfs.length; i++){
			tline.add(new Pair<String,String>(pfs[i].getname(),pfs[i].getValue(aToken,eM)));
		}
		
		InstanceToken iToken;
		if(addlabel)
			iToken = new InstanceToken("",tline,pairlabel.getValue(aToken,eM));
		else
			iToken = new InstanceToken("",tline);
		return iToken;
	}
	public LinkedList<Instance> buildPairfeature(AnnotatedSentence aSen,Boolean addlabel){
		LinkedList<Instance> iList = new LinkedList<Instance>();
		Iterator<AnnotatedToken> it = aSen.iterator();
		it.next();//skip the first root token
		while(it.hasNext()){
			AnnotatedToken a = it.next();
			if(!a.getEvent_type().equals("NOTEVENT")){
				for(EntityMention em: aSen.getMentions()){
					iList.add(this.buildfeature(a, em,addlabel));
				}
			}
		}
		return iList;
	}
	public LinkedList<LinkedList<Instance>> buildfeature(AnnotatedDoc aDoc,Boolean addlabel){
		this.metadata = aDoc.getMetadata();
		LinkedList<LinkedList<Instance>> ilList = new LinkedList<LinkedList<Instance>>();
		for(AnnotatedSentence aSen: aDoc){
			ilList.add(buildfeature(aSen,addlabel));
		}
		return ilList;
	}
	
	
	/** Need Modification
	 * @param aCorpus
	 * @return
	 */
	// TODO This is just a test，Need Modification
	public LinkedList<LinkedList<Instance>> buildfeature(LinkedList<AnnotatedDoc> aCorpus, Boolean addlabel){
		LinkedList<LinkedList<Instance>> ilList = new LinkedList<LinkedList<Instance>>();
		for(AnnotatedDoc aDoc: aCorpus){
			ilList.addAll(this.buildfeature(aDoc,addlabel));
		}
		return ilList;
	}
	
	public void outputPair(String des, Boolean addlabel,LinkedList<AnnotatedDoc> aCorpus){
		log.info("Pair-Feature Generating.");
		if(addlabel)
			log.info("Output Pair-feature-enhanced file with label to: "+ des);
		else
			log.info("Output Pair-feature-enhanced file without label to: "+des);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(des)));
			for(AnnotatedDoc aDoc: aCorpus){
				for(AnnotatedSentence aSen: aDoc)
					bw.append(format.wholeFormat(this.buildPairfeature(aSen, addlabel)));
			}
			bw.flush();
			bw.close();
			log.info("Pair-Feature-enhanced file outputed.");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error:"+e.getStackTrace().toString());
		}
		log.info("Pair-Feature Generating finshed");
	}
	public void output(String des, Boolean addlabel,AnnotatedDoc aDoc){
		log.info("Feature Generating.");
		if(addlabel)
			log.info("Output feature-enhanced file with label to: "+ des);
		else
			log.info("Output feature-enhanced file without label to: "+des);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(des)));
			for(AnnotatedSentence aSen: aDoc)
				bw.append(format.wholeFormat(this.buildfeature(aSen, addlabel)));
			bw.flush();
			bw.close();
			log.info("Feature-enhanced file outputed.");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error:"+e.getStackTrace().toString());
		}
		log.info("Feature Generating finshed");
	}
	/** Build feature and output it to file, format of the file is defined by format field.
	 * @param des output destination file directory
	 * @param label boolean indicate final output will or will not have label field
	 */
	public void output(String des, Boolean addlabel,LinkedList<AnnotatedDoc> aCorpus){
		log.info("Feature Generating.");
		if(addlabel)
			log.info("Output feature-enhanced file with label to: "+ des);
		else
			log.info("Output feature-enhanced file without label to: "+des);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(des)));
			for(AnnotatedDoc aDoc: aCorpus){
				for(AnnotatedSentence aSen: aDoc)
					bw.append(format.wholeFormat(this.buildfeature(aSen, addlabel)));
			}
			bw.flush();
			bw.close();
			log.info("Feature-enhanced file outputed.");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error:"+e.getStackTrace().toString());
		}
		log.info("Feature Generating finshed");
	}
}
