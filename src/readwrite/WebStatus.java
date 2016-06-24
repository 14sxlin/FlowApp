package readwrite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Timer;

public class WebStatus implements ActionListener {
	/*
	 * 1.记得退出的时候把时间关掉
	 * 2.不知道为什么并不能截取到密码错误的情况
	 */
	public static final int IN=1;
	public static final int OUT=0;
	public static final int ERROR=-1;//获取服务器的时候发生错误
	public String userName;
	public int usedAmount; 
	public int remainAmount;
	public int totalAmount;
	public boolean isWebLost;
	public int loginStatus;
	public boolean useOut=false;
	private StringBuilder htmldata;
	public Timer timer;
	private final String serverURL;
	
	public WebStatus(String serverURL) throws IOException {
		this.serverURL = serverURL;
		try {
			timer=new Timer(1000, this);
			timer.start();		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginStatus=OUT;
			setNull();
		}
	}
	
	/**
	 * 连接到网络的服务器,获取html页面中的数据
	 * 保存到成员变量里面
	 * @param serverURL 服务器的URL
	 */
	public void loadHtml() {
		BufferedReader br=null;
		try {
			URL url=new URL(serverURL);
			URLConnection con=url.openConnection();
			con.setConnectTimeout(1000);
			InputStream in=con.getInputStream();
			br=new BufferedReader(new InputStreamReader(in));
			String line;
			htmldata = new StringBuilder();
			while ((line = br.readLine()) != null) {
				htmldata.append(line);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			isWebLost=true;
			setNull();
		}		
	}
	
	/**
	 * 更新所有的内部的状态
	 * @param input
	 * @throws IOException
	 */
	private  void loadAllStatus() throws IOException
	{
		loadHtml();
		setNull();
		setWebLost();
		this.setLoginStatus();
		this.setUseOut();
		if(loginStatus==IN)
		{	userName=loadUserName();
			usedAmount=loadUsedAmount();
			totalAmount=loadTotalAmount();
			remainAmount=loadRemainAmount();
		}
	}
	
	/**
	 * 把数据值恢复到默认状态
	 */
	public  void setNull()
	{
		userName="";
		usedAmount=0;
		totalAmount=0;
		remainAmount=0;
	}	
	
//	/**
//	 * 通过正则表达式获取某个标签内的数据
//	 * @param input
//	 * @param pattern
//	 * @return 匹配的值
//	 */
//	@Deprecated
//	private  String cutNumberByMacther(String pattern)
//	{
//		String temp1 = null;
//		try {//这在点击退出的时候回触发这个错误
//			String temp = null;
//			Pattern p=Pattern.compile(pattern);
//			Matcher m=p.matcher(htmldata);
//			while(m.find())
//			{
//				temp=htmldata.substring(m.start(), m.end());
//			}
//			p=Pattern.compile("\\d{0,3},?\\d{1,3},\\d{1,3}");
//			m=p.matcher(temp);
//			temp1 = "没有找到内容";
//			while(m.find())
//			{
//				temp1=temp.substring(m.start(), m.end());
//			}
//		} catch (NullPointerException e) {
////			timer.stop();
//			setNull();
//		}
////System.out.println("temp1="+temp1);
//		if(temp1.length()<=7)
//			return "0,"+temp1;
//		else return temp1;
//	}
	
	/**
	 * 通过标签来定位,获取标签内部的值
	 * @return 返回标签内的内容
	 */
	private String cutNumberByLabel(String left,String right)
	{
		int first = htmldata.indexOf(left)+left.length();
		if(first==-1) return "0";
		int end = htmldata.indexOf(right, first);
		if(end!=-1)
		{
			return htmldata.substring(first,end);
		}
		else return "0";
	}
	/**
	 * 获取网页中的用户名
	 * 内建了pattern 
	 * 以后的话要用配置文件进行配置
	 * @param input html页面的数据
	 * @return
	 */
	private  String loadUserName()
	{
		if (!isWebLost) {
			String temp = "";
			Pattern p = Pattern.compile("<td width=\\\"262\\\" class=\\\"text3\\\">.+?</td>");
			Matcher m = p.matcher(htmldata);
			while (m.find())
				temp += htmldata.substring(m.start(), m.end());
			p = Pattern.compile(">.+?<");
			m = p.matcher(temp);
			String temp1 = "";
			while (m.find())
				temp1 += temp.substring(m.start(), m.end());
			if (temp != "")
			{
				return temp1.substring(1, temp1.length() - 1);
			}
			else
				return "";
		}else return "";
	}
	
	/**
	 * 获取网页中的总的流量
	 * 内建了正则表达式
	 * @param input
	 * @return
	 */
	private  int loadTotalAmount()
	{
//		String rslt = cutNumberByMacther("<td class=\\\"text3\\\" id=\\\"tb\\\">.+?</td>");
		String rslt = cutNumberByLabel("<td class=\"text3\" id=\"tb\">", "<");
		return formatFlowData(rslt);
	}
	
	/**
	 * 获取网页中的已使用的流量
	 * @param input
	 * @return
	 */
	private  int loadUsedAmount()
	{
//		String rslt = cutNumberByMacther("<td class=\\\"text3\\\" id=\\\"ub\\\">.+?</td>");		
		String rslt = cutNumberByLabel("<td class=\"text3\" id=\"ub\">", "<");
		return formatFlowData(rslt);
	}

	/**
	 * 通过总的流量和已使用的流量计算剩余的流量
	 * @return
	 * @throws IOException
	 */
	private  int loadRemainAmount() throws IOException
	{
		setUseOut();
		if(useOut)
			return 0;				
		else if(loginStatus==IN) {
			return totalAmount-usedAmount;
		}
		return -1;
	}
	
	/**
	 * 将以 ***,***,*** 形式的流量字符串转化成数字
	 * @param data 要转化的字符串
	 * @return 返回相应的流量值,单位是M,如果出错的话返回-1
	 */
	public int formatFlowData(String data)
	{
		String[] flow = data.split(",");
		try {
			int reslt = 0;
			int count = 1;
			for(int i=flow.length-1;i>=0;i--) {
				reslt+=(Integer.parseInt(flow[i])*count);
				count*=1000;
			}
			return reslt/1000000;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return -1;
		
		
	}
	
	/**
	 * 标记流量是否用完
	 */
	public  void setUseOut()
	{
		if(!isWebLost)
		{	this.setLoginStatus();
			if(loginStatus==IN)
			{	
				if(totalAmount<=usedAmount)
					useOut = true;
				else useOut = false;
			}
		}
	}
	
	/**
	 * 更新类中登录状态
	 */
	private void setLoginStatus()
	{
		if(!isWebLost)
		{try {
				loadHtml();
				Pattern p=Pattern.compile("<h3><center><font color=\\\"red\\\" style=\\\"display:[a-z]*?\\\">");
				Matcher m=p.matcher(htmldata);
				String temp="none";
				while(m.find())
					temp=htmldata.substring(m.start(),m.end());
				String temp2=null;
				int index=temp.indexOf("display:");
				if(index!=-1)
					temp2=temp.substring(index+8, index+9);
				if(temp2.equals("n"))
					loginStatus=OUT;
					else if(temp2.equals("i"))
						loginStatus=ERROR;
					else 	loginStatus=IN;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				loginStatus=IN;
			}
		}
	}

	public void setWebLost()
	{
		isWebLost=false;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
				loadAllStatus();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
