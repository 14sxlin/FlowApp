package readwrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import base64.Base64Coder;
import tool.MyLogger;


/**
 * 
 * @author LinSixin sparrowxin@sina.cn
 * �˻�������,�ṩ���˻��Ĳ���
 */
public class AccountManager {

	/**
	 * �������˺ŵ��ļ�
	 */
	private File file;
	/**
	 * jar����ϵͳ�еĵ�ַ
	 */
	private String jarPath;
	/**
	 * ��������û����Ͷ�Ӧ��Ҫ���͵ı�����
	 */
	public  HashMap<String,String> accountMap;
	/**
	 * �������û�������
	 */
	public  ArrayList<String> usernameList;
	public String[] usernames;
	/**
	 * ָ���ļ�������·��
	 * @param jarPath
	 */
	public AccountManager(String jarPath ,String fileName) {
		this.jarPath = jarPath+"/"+fileName;
		usernameList = new ArrayList<>();
		accountMap = new HashMap<>();
		loadAccounts();
	}
	public AccountManager(String filePath) {
		this.jarPath = filePath;
		usernameList = new ArrayList<>();
		accountMap = new HashMap<>();
		loadAccounts();
	}

	/**
	 * ��ȡ�˻��ļ��е��˺�,���浽�б��������ȥ
	 * @throws IOException
	 */
	public void loadAccounts() {
		try {
			usernameList = new ArrayList<String>();
			accountMap = new HashMap<String, String>();
			File f = new File(decode(jarPath));
			if (!f.exists())
				f.createNewFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line;
			while ((line = br.readLine()) != null) {
				String parser = Base64Coder.decode(line.trim());
				accountMap.put(this.getAccountName(parser), parser);
				usernameList.add(this.getAccountName(parser).trim());
			}
			br.close();
			generateArray(usernameList);
		} catch (IOException e) {
			MyLogger.fatal(getClass(), e.getMessage()+" ��ȡ�û���Ϣʧ��");
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�û�������
	 * @param line ��ʽӦ���� username=***&password=*****
	 * @return �����û������� username����Ĳ���
	 */
	private String getAccountName(String line)
	{
		String temp[]=line.split("&");
		int index=temp[0].indexOf("=");
		return temp[0].substring(index+1);
	}
	
	/**
	 * �����û��б��е�����,�����ڴ��в���
	 * @param removeInt ���ĵ���Ŀ�����
	 * @param newname �µ��û���
	 * @param newpassword �µ�����
	 */
	public void update(int removeInt,String newname,String newpassword)
	{
		if (removeInt<usernameList.size()) {
			usernameList.set(removeInt, newname);
			accountMap.put(newname, "AuthenticateUser=" + newname + "&" + "AuthenticatePassword=" + newpassword + "&shit");
			generateArray(usernameList);
		}
	}
	
	/**
	 * ɾ���û�,�����ڴ��в���
	 * @param removeInt
	 * @return ���ر�ɾ�����û���
	 */
	public String drop(int removeInt)
	{
		if (removeInt<usernameList.size()) {
			String name = usernameList.get(removeInt);
			usernameList.remove(removeInt);
			accountMap.remove(name);
			generateArray(usernameList);
			return name;
		}
		return null;
	}
	
	/**
	 * ������˻����б��ĩβ
	 * ��������ݱ����Ѿ��е�,�͸�������
	 * �������ĩβ���
	 * @param name
	 * @param password
	 */
	public void add(String name,String password) {
		if (usernameList.contains(name)) {
			int index = usernameList.indexOf(name);
			update(index, name, password);
		}else{
			String str = "";
			str += "AuthenticateUser=" + name + "&" + "AuthenticatePassword=" + password + "&shit";
			accountMap.put(name, str);
			usernameList.add(name);
		}
		generateArray(usernameList);
	}
	
	/**
	 * ������е�����
	 */
	public void clearAll() {
		usernameList.clear();
		accountMap.clear();
		generateArray(usernameList);
	}
	
	/**
	 * ��������д����ӵ��ļ���
	 * @param url���Ĳ���
	 */
	public void writeAccount(String params)
	{
		try {
			PrintWriter out = null;
			File f=new File(jarPath);//��仰�������½��ļ�
			OutputStream in=new FileOutputStream(f,true);//���ļ�����׷������,������仰�����Զ��½���һ��
			out=new PrintWriter(in);
			out.append(Base64Coder.encode(params)+"\r\n");
			out.close();
			generateArray(usernameList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * �����ļ��е�����,�Լ��û��б��е�����
	 * @param removeInt �Ƴ������ 
	 * @param name �µ�����
	 * @param password �µ�����
	 * @throws IOException 
	 */
	public void updateFile()
	{
		try {
			file = new File(decode(jarPath));
			if (file.exists())//ɾ�������½����ļ� ������������
				file.delete();
			file.createNewFile();
			for (String key : usernameList) {
				writeAccount(accountMap.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	  ���������ļ�·�����б���,��ʶ��������Ļ����ǿո�
	 * ���ļ�·��
	 * @param path �ļ�·�� ���ܰ����ո������
	 * @return ת�����·��,��ȷ��ʾ���ĺͿո�
	 * @throws UnsupportedEncodingException
	 */
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}
	
	/**
	 * ���û������б�ת�����ַ�������
	 * �����ڳ�Ա������
	 * @param list  �û����б�
	 */
	private void generateArray(ArrayList<String>list) {
		usernames = new String[list.size()];
		list.toArray(usernames);
	}
}
