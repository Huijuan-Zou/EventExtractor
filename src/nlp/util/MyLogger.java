package nlp.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class MyLogger{

	public MyLogger(){
		//log.addAppender(new FileAppender());
		PropertyConfigurator.configure("log4j.properties");
	}
   /* Get actual class name to be printed on */
    public Logger log = Logger.getLogger(MyLogger.class.getName());
}
