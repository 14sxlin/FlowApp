package deprecated;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

import readwrite.ResourcePath;
@Deprecated
public class WriteAccount implements ResourcePath{
	public  PrintWriter out;
	/*
	 * �����Ҫ�ǵùر���
	 */
	public WriteAccount() throws UnsupportedEncodingException  {
		// TODO Auto-generated constructor stub

		out=this.openStream(decode(ACCOUNTPATH));
	}
	
	public PrintWriter openStream(String path)
	{
		PrintWriter out=null;
		try {
			File f=new File(path);//��仰�������½��ļ�
			OutputStream in=new FileOutputStream(f,true);//���ļ�����׷������,������仰�����Զ��½���һ��
			out=new PrintWriter(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "���ļ�����ʧ��ʧ��\n"+this.getClass().getName());
		}
		return out;		
	}

	public void writeAccount(PrintWriter out,String name,String password)
	{
		String str="";
		str+="AuthenticateUser="+name+"&"+"AuthenticatePassword="+password+"&shit";
		out.append(str+"\r\n");
		out.close();
	}
	public void writeAccount(PrintWriter out,String params)
	{
		out.append(params+"\r\n");
	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}
}
