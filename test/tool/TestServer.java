package tool;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Ignore;
import org.junit.Test;

@Deprecated
public class TestServer {

	@Test
	@Ignore
	public void test() throws UnsupportedEncodingException {
		String r = "";
		System.out.println(r+=new Server().getServerPath());
		Server server = new Server();
		server.openWebsite();
		System.out.println(URLDecoder.decode(server.toString(),"utf-8"));
		assertEquals("http://192.168.9.30:8080", r);
	}
	
	@Test
	public void useHttpClient() throws Exception {
//		HttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost("http://1.1.1.2/ac_portal/login.php");
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
//        nvps.add(new BasicNameValuePair("opr", "pwdLogin"));  
//        nvps.add(new BasicNameValuePair("userName", "14sxlin"));  
//        nvps.add(new BasicNameValuePair("pwd", "WoHen2"));  
//        nvps.add(new BasicNameValuePair("rememberPwd", "0"));  
//        post.setEntity(new UrlEncodedFormEntity(nvps));  
//		HttpResponse response = client.execute(post);
//		HttpEntity entity = response.getEntity();
//		System.out.println(entity.getContentType());
//		System.out.println(entity.getContentEncoding());
//		if(entity!=null)
//		{
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(entity.getContent()));
//			String line = "";
//			while((line = bufferedReader.readLine())!=null)
//			{
//				System.out.println(URLDecoder.decode(line,"utf-8"));
//			}
//		}
//		System.out.println("-------header------------");
//		HeaderIterator it = response.headerIterator("Set-Cookie");
//		while(it.hasNext())
//		{
//			System.out.println(it.next());
//		}
		
//		HttpPost post2 = new HttpPost("http://1.1.1.2/ac_portal/userflux");
//		HttpResponse response2 = client.execute(post2);
//		HttpEntity entity2 = response2.getEntity();
//		if(entity2!=null)
//		{
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(entity2.getContent(),"utf-8"));
//			String line = "";
//			while((line = bufferedReader.readLine())!=null)
//			{
//				System.out.println("line = "+line);
//			}
//		}
	}

}
