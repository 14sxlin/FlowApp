package readwrite;

import java.io.UnsupportedEncodingException;

public interface ResourcePath {
	public static final String SERVERPATH="http://192.168.31.4:8080/?status=ok&url=";
	public static final String DATAPATH="http://192.168.31.4:8080";
	public static final String JARPATH=ClassLoader.getSystemResource("").getPath();
	public static final String ACCOUNTPATH=JARPATH+"account.txt";
	public static final String CONFIGPATH=JARPATH+"config.properties";
	public String decode(String path) throws UnsupportedEncodingException;
}
