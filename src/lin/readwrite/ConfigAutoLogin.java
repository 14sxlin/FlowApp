package lin.readwrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ConfigAutoLogin implements ResourcePath{
	private String jarPath;
	private BufferedReader br;
	private PrintWriter pw;
	public ConfigAutoLogin() throws IOException {
		// TODO Auto-generated constructor stub
		jarPath=this.decode(ResourcePath.CONFIGPATH);
	}
	/*
	 * ʹ�þֲ���file�������Զ�����,û��ʹ�õĻ�Ҳ���ᴴ��
	 * ���ǲ�֪��Ϊʲôʹ�ó�Ա����û�취�����ļ�?
	 */
	
	public BufferedReader openFile() throws IOException
	{	
		try {
			File f=new File(jarPath);
			if(!f.exists())
				f.createNewFile();
			br=new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}
	
	public int readModel()
	{
		try {
			openFile();
			String temp="none";
			temp=br.readLine();
			br.close();
			if(temp==null)
				return 0;
			String model=temp.substring(0, 1);
			if(model.equals("!"))
				return 1;
			else if(model.equals("?"))
				return -1;
			else if(model.equals("#"))
				return 2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public String readName() throws IOException
	{
		openFile();
		String temp=br.readLine();
		br.close();
		return temp.substring(1);
	}
	//д��?����û�п����Զ���¼�Ĺ���,����˺����˳��˺ŵ�ʱ��д���
	public void write_1Name(String name)
	{
		try {
			File f=new File(jarPath);
			pw=new PrintWriter(new FileOutputStream(f));//û�п���׷�ӹ���,�Ḳ��,��ζ���ļ���ֻ����һ���˺�
			pw.println("?"+name);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//���д�����!��,��ʾ1��  open ָ������û�п����Զ��л��˺�
	public void write1Name(String name)
	{
		try {
			File f=new File(jarPath);
			pw=new PrintWriter(new FileOutputStream(f));
			pw.println("!"+name);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//���д#����2	open ָ������û�п����Զ��л��˺�
	public void write2Name(String name)
	{
		try {
			File f=new File(jarPath);
			pw=new PrintWriter(new FileOutputStream(f));
			pw.println("#"+name);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Deprecated
	public char writeSeletMark(boolean open)
	{
		if(open)
			return 'Y';
		else return 'N';
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ConfigAutoLogin();
	}
	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}

}
