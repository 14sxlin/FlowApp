package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



@SuppressWarnings("deprecation")
public class RequestSender {
	public RequestSender() throws IOException {}
	
	public static String login(String serverpath,List<NameValuePair> params) throws IOException
	{
		String info ="";
		@SuppressWarnings("resource")
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(serverpath);
        post.setEntity(new UrlEncodedFormEntity(params));  
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if(entity!=null)
		{
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(entity.getContent(),"utf-8"));
			String line = "";
			while((line = bufferedReader.readLine())!=null)
			{
				info+=line;
			}
		}
		return info;
	}
	
	public static void logout(String serverpath) throws IOException
	{
		List<NameValuePair> param = new ArrayList<>();
		param.add(new BasicNameValuePair("opr", "logout"));
		login(serverpath, param);
	}
}
