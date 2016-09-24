package readwrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


@SuppressWarnings("deprecation")
public class UseInfo {
	public static boolean isLogin ;
	public static String userName ="";
	public static int used = -1 ;
	public static int total = -1;
	public static int remain = -1;
	public static boolean useOut = false;
	
	private static void init(){
		userName ="";
		used = -1 ;
		total = -1;
		remain = -1;
		useOut = false;
	}
	
	public static String readRawInfo() throws ClientProtocolException, IOException {
		String rlst ="";
		@SuppressWarnings("resource")
		HttpClient client = new DefaultHttpClient();
		HttpPost post2 = new HttpPost("http://1.1.1.2/ac_portal/userflux");
		HttpResponse response2 = client.execute(post2);
		HttpEntity entity2 = response2.getEntity();
		if(entity2!=null)
		{
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(entity2.getContent(),"utf-8"));
			String line = "";
			
			while((line = bufferedReader.readLine())!=null)
			{
				rlst+=line;
			}
		}
		return rlst;
	}
	
	public static void parseInfo(String rawInfo) throws ClientProtocolException, IOException{
		int s = 0,e = 0;
		String rlst = readRawInfo();
		int count = 0;
		for(;s!=-1;s=rlst.indexOf("<td>",s+1),e = rlst.indexOf("</td>",s))
		{
			int i = rlst.substring(s,e).indexOf(">");
			int length = rlst.substring(s,e).length();
			if(count==2)
			{	userName = rlst.substring(s,e).substring(i+1,length);
			}
			if(count==4)
			{
				total = Integer.parseInt(rlst.substring(s,e).substring(i+1,length-1))*1024;
			}
			if(count==6)
			{
				int end = rlst.substring(s,e).indexOf(".");
				used = Integer.parseInt(rlst.substring(s,e).substring(i+1,end));
			}
			count++;
		}
		if(total!=-1)
			remain = total - used;
		else
			remain = -1;
	}
	
	
	public static void Refresh() throws ClientProtocolException, IOException{
		init();
		parseInfo(readRawInfo());
		if(userName!=null&&!userName.trim().equals(""))
		{
			isLogin = true;
		}else{
			isLogin = false;
		}
		if(used!=-1&&total!=-1&&remain<=0)
		{
			useOut =true;
		}
	}
}
