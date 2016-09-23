package readwrite;

import java.io.UnsupportedEncodingException;

public interface ResourcePath {
	public static final String STU_IMAGE_PATH = "/img/";
	public static final String SERVERPATH = "http://1.1.1.2/ac_portal/login.php";
	public static final String DATAPATH = "http://1.1.1.2/ac_portal/useflux";
	public static final String JARPATH=ClassLoader.getSystemResource("").getPath();
	public static final String ACCOUNTPATH=JARPATH+"account.txt";
	public static final String CONFIGPATH=JARPATH+"config.properties";
	public String decode(String path) throws UnsupportedEncodingException;
}
