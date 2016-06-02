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

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class WebStatus implements ActionListener {
	/*
	 * 1.�ǵ��˳���ʱ���ʱ��ص�
	 * 2.��֪��Ϊʲô�����ܽ�ȡ�������������
	 */
	public static final int IN=1;
	public static final int OUT=0;
	public static final int ERROR=-1;//��ȡ��������ʱ��������
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
	 * ���ӵ�����ķ�����,��ȡhtmlҳ���е�����
	 * ���浽��Ա��������
	 * @param serverURL ��������URL
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
			System.out.println(htmldata);
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "�Ѷ���~��ú���Ϣ,��");
			isWebLost=true;
			setNull();
		}		
	}
	
	/**
	 * �������е��ڲ���״̬
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
	 * ������ֵ�ָ���Ĭ��״̬
	 */
	public  void setNull()
	{
		userName="";
		usedAmount=0;
		totalAmount=0;
		remainAmount=0;
	}	

	/*
	public BufferedReader openStream(String pathname) throws IOException
	{
		BufferedReader br=null;
		try {
			URL url=new URL(pathname);
			URLConnection con=url.openConnection();
			con.setConnectTimeout(1000);
			InputStream in=con.getInputStream();
			br=new BufferedReader(new InputStreamReader(in));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "�Ѷ���~��ú���Ϣ,��");
//			timer.stop();
			isWebLost=true;
			setNull();
		}		
		return br;
		
	}
	
	public StringBuilder getStringBuilder(BufferedReader br) throws IOException
	{
		if (!isWebLost) {
			String line;
			StringBuilder b = new StringBuilder();
			while ((line = br.readLine()) != null) {
				b.append(line);
			}
			br.close();
			return b;
		}else return new StringBuilder("");
	}*/
	
	/**
	 * ͨ��������ʽ��ȡĳ����ǩ�ڵ�����
	 * @param input
	 * @param pattern
	 * @return
	 */
	private  String cutNumberByMacther(String pattern)
	{
		String temp1 = null;
		try {//���ڵ���˳���ʱ��ش����������
			String temp = null;
			Pattern p=Pattern.compile(pattern);
			Matcher m=p.matcher(htmldata);
			while(m.find())
			{
				temp=htmldata.substring(m.start(), m.end());
			}
			p=Pattern.compile("\\d{0,3},?\\d{1,3},\\d{1,3}");
			m=p.matcher(temp);
			temp1 = "û���ҵ�����";
			while(m.find())
			{
				temp1=temp.substring(m.start(), m.end());
			}
		} catch (NullPointerException e) {
//			timer.stop();
			setNull();
		}
//System.out.println("temp1="+temp1);
		if(temp1.length()<=7)
			return "0,"+temp1;
		else return temp1;
	}
	
	/**
	 * ��ȡ��ҳ�е��û���
	 * �ڽ���pattern 
	 * �Ժ�Ļ�Ҫ�������ļ���������
	 * @param input htmlҳ�������
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
	 * ��ȡ��ҳ�е��ܵ�����
	 * �ڽ���������ʽ
	 * @param input
	 * @return
	 */
	private  int loadTotalAmount()
	{
		String rslt = cutNumberByMacther("<td class=\\\"text3\\\" id=\\\"tb\\\">.+?</td>");
		return formatFlowData(rslt);
	}
	
	/**
	 * ��ȡ��ҳ�е���ʹ�õ�����
	 * @param input
	 * @return
	 */
	private  int loadUsedAmount()
	{
		String rslt = cutNumberByMacther("<td class=\\\"text3\\\" id=\\\"ub\\\">.+?</td>");		
		return formatFlowData(rslt);
	}

	/**
	 * ͨ���ܵ���������ʹ�õ���������ʣ�������
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
	 * ���� ***,***,*** ��ʽ�������ַ���ת��������
	 * @param data Ҫת�����ַ���
	 * @return ������Ӧ������ֵ,��λ��M,�������Ļ�����-1
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
	 * ��������Ƿ�����
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
	 * �������е�¼״̬
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

	@Deprecated
	public void setWebLost()
	{
//		long time=System.currentTimeMillis();
//		SimpleDateFormat str=new SimpleDateFormat("hh:mm:ss");
//System.out.println("down is: "+str.format(new Date(0-8*60*60*1000)));
//System.out.println(1440609223319l/60/60/1000/24/365);
//System.out.println(6*60*60*1000+30*60*1000);
//System.out.println("now is:  "+str.format(new Date(time)));
//System.out.println("up is  :  "+str.format(new Date(0-8*60*60*1000+6*60*60*1000+30*60*1000)));
//		if(time>=(0-8*60*60*1000)&&time<=6*60*60*1000+30*60*1000)
//			WebLost=true;
//		else WebLost=false;
		isWebLost=false;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
				loadAllStatus();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "��ҳ�򲻿���..��������\n"+this.getClass().getName());
		}
	}
}
