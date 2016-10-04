package nlp.model;

import java.io.IOException;

public interface Model {
	public void train(String trainfile, String modelpath) throws IOException;
	public void predict(String modelpath, String testfile) throws IOException;
	public Float getRecall();
	public Float getPrecision();
	public Float getAccuracy();
	public Float getFvalue(int b);
}
