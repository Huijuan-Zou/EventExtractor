package nlp.model.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Bruce Yang
 * @author Robin (Some modification)
 */
public class UnixScript {
	private static ProcessBuilder pB;
	private StringBuilder stdout;
	private StringBuilder stderr;
	private String pre;
	public UnixScript(String f) throws IOException{
		this.pre = f;
		pB = new ProcessBuilder();
		pB.directory(new File("./"));
		//pB.redirectOutput(new File("/Users/Robin/Documents/Program/EclipseWorkSpace/EventExtractor/out"));
		//pB.redirectError(new File("/Users/Robin/Documents/Program/EclipseWorkSpace/EventExtractor/err"));
		stdout = new StringBuilder();
		stderr = new StringBuilder();
	}
	 /**
	 * 执行 mac(unix) 脚本命令~
	 * @param command
	 * @return
	 * @throws IOException 
	 */
	public void execute(ArrayList<String> commands) throws IOException {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(pre + commands.get(0));
		Iterator<String> it = commands.iterator();
		it.next();
		while(it.hasNext())
			temp.add(it.next());
		pB.command(temp);
		
		Process proc = pB.start();
		stdout = new StringBuilder();
		stderr = new StringBuilder();
		BufferedReader stdInput = new BufferedReader(new 
		InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new 
		InputStreamReader(proc.getErrorStream()));
		// read the output from the command
		//System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null) {
			stdout.append(s+'\n');
		}
		// read any errors from the attempted command
		//System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
			stderr.append(s+'\n');
		}
	}

  /**
  * 读取控制命令的输出结果
  * 原文链接：http://lavasoft.blog.51cto.com/62575/15599
  * @param cmd 命令
  * @return 控制命令的输出结果
  * @throws IOException
  */
	public String getStdout() {
		return stdout.toString();
	}
	public String getStderr() {
		return stderr.toString();
	}
  
  public static void main(String[] args) throws Exception{
	  UnixScript ux = new UnixScript("/Users/Robin/lamachine/bin/");
	  
  }
}
