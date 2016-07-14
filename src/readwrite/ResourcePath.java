package readwrite;

import java.io.UnsupportedEncodingException;

import tool.Server;

public interface ResourcePath {
	public static final String SERVERPATH=new Server().getServerPath()+"/?status=ok&url=";
	public static final String DATAPATH=new Server().getServerPath();
	public static final String JARPATH=ClassLoader.getSystemResource("").getPath();
	public static final String ACCOUNTPATH=JARPATH+"account.txt";
	public static final String CONFIGPATH=JARPATH+"config.properties";
	public String decode(String path) throws UnsupportedEncodingException;
}
