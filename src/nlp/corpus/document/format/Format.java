package nlp.corpus.document.format;

import java.io.File;
import java.util.LinkedList;

import nlp.event.instance.Instance;
import nlp.util.Pair;

public interface Format {
	/** Generate one well formatted instance.
	 * @param instance	Name of the instance to be added.
	 * @param label		label of this instance.
	 * @param line		features of this instance. Pair.first is the name of feature, Pair.second is the value
	 * @return			Formatted instance.
	 */
	public String oneFormat(String instance,String label,LinkedList<Pair<String,String>> line);
	public String oneFormat(Instance instance);
	public String wholeFormat(LinkedList<Instance> instanceList);
	/**Add the instance to the end of a formated file.
	 * @param f			Formatted file to be append.
	 * @param instance	Name of the instance to be added.
	 * @param label		label of this instance.
	 * @param line		features of this instance. Pair.first is the name of feature, Pair.second is the value
	 */
	public void append(File f,String instance,String label,LinkedList<Pair<String,String>> line);
}
