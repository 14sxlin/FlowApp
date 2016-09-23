package tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ParamsAdapter {
	
	
	public static List<NameValuePair> String2List(String params){
		
		String name,pwd;
		name = SubString(params, "AuthenticateUser");
		pwd = SubString(params, "AuthenticatePassword");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		nvps.add(new BasicNameValuePair("opr", "pwdLogin"));  
		nvps.add(new BasicNameValuePair("userName", name));  
		nvps.add(new BasicNameValuePair("pwd", pwd));  
		nvps.add(new BasicNameValuePair("rememberPwd", "0")); 
		return nvps;
	}
	
	public static String SubString(String param,String name){
		int first = param.indexOf(name);
		if(first==-1)
			return "";
		int firstend = param.indexOf("&",first);
		if(firstend!=-1)
			return param.substring(first+name.length()+1, firstend);
		else
			return param.substring(first+name.length()+1);
	}
}
