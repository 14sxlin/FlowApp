package tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

public class TestRequestSender {

	RequestSender sender  ;
	@Before
	public void setUp() throws Exception {
		sender = new RequestSender();
	}

	@Test
	public void test() {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		nvps.add(new BasicNameValuePair("opr", "pwdLogin"));  
		nvps.add(new BasicNameValuePair("userName", "14sxlin"));  
		nvps.add(new BasicNameValuePair("pwd", "WoHen2"));  
		nvps.add(new BasicNameValuePair("rememberPwd", "0"));  
		try {
			String response = RequestSender.login("http://1.1.1.2/ac_portal/login.php", nvps);
			System.out.println("TestRequestSender.test() "+response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
