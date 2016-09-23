package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class Server implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String loginURL;
	public String infoURL;
	
	private StringBuilder sb;
	
	public Server() {
		sb = new StringBuilder();
	}
	
	/**
	 * ��ȡ��������URL
	 * @return
	 */
	@Deprecated
	public String getServerPath()
	{
		openWebsite();
		return getFrameSrc();
	}
	
	@Deprecated
	public void openWebsite() {
		URL url = null;
		try {
//			url=new URL("http://internet.stu.edu.cn/");
			url=new URL("http://1.1.1.2/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(1000);
			con.setReadTimeout(1000);
			con.connect();
			InputStreamReader in = 
					new InputStreamReader(con.getInputStream());
			BufferedReader br = new BufferedReader(in);
			sb = new StringBuilder();
			String temp = "";
			while((temp=br.readLine()) != null)
			{
				sb.append(temp);
			}
			con.disconnect();
			MyLogger.debug(getClass(), 
					"  "+con.getResponseCode()+"  "+sb.toString());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			MyLogger.fatal(getClass(), e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.fatal(getClass(), e.getMessage());
		}
	}
	
	/**
	 * ��ȡ��ҳ�е�iframe��ǩ��src�����Ե�ֵ
	 * @return
	 */
	@Deprecated
	private String getFrameSrc() {
		String mark = "<iframe width=\"100%\" height=\"100%\" src=\"";
		int frameIndex =
				sb.lastIndexOf(mark);
		if(frameIndex==-1)
		{	
			MyLogger.info(getClass(), "can not find mark");
			return null;
		}
		int end = sb.indexOf("\"", frameIndex+mark.length()+1);
		return sb.substring(frameIndex+mark.length(), end);
	}
	
}
