package nlp.event.Feature;

import nlp.annotator.util.AnnotatedToken;

public abstract class CompoundFeature {

	public CompoundFeature() {
		// TODO Auto-generated constructor stub
	}
	
	abstract public String getMineName();
	/**
	 * @return list of name, delimited by:
	 */
	abstract public String getname();
	/**
	 * @return list of value, delimited by:
	 */
	abstract public String getValue(AnnotatedToken t);
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
