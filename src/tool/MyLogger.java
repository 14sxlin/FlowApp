package tool;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public abstract class MyLogger {
	private static Logger logger = null;
	
	public static void loadConfigure() {
		PropertyConfigurator.configure(
				MyLogger.class.getResourceAsStream("/log4j.properties"));
	}
	public static void setLogger(Class<?> myclass) {
			logger = Logger.getLogger(myclass);
	}
	
	public static void info(String message) {
		logger.info(message);
	}
	
	public static void warn(String message) {
		logger.warn(message);
	}
	
	public static void fatal(String message) {
		logger.fatal(message);
	}
	public static void fatal(Class<?> myclass,String message) {
		setLogger(myclass);
		logger.fatal(message);
	}
}
