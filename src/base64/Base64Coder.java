package base64;

import org.bouncycastle.util.encoders.Base64;

public abstract class Base64Coder {
	public final static String ENCODING = "utf-8";
	public static String encode(String data) {
		return Base64.toBase64String(data.getBytes());
	}
	
	public static String decode(String data) {
		return new String(Base64.decode(data));
	}
	
}
