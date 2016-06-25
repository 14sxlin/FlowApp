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
 * 账户管理类,提供对账户的操作
 */
public class AccountManager {

	/**
	 * 保存了账号的文件
	 */
	private File file;
	/**
	 * jar包在系统中的地址
	 */
	private String jarPath;
	/**
	 * 保存的是用户名和对应的要发送的表单数据
	 */
	public  HashMap<String,String> accountMap;
	/**
	 * 保存了用户的名称
	 */
	public  ArrayList<String> usernameList;
	public String[] usernames;
	/**
	 * 指定文件操作的路径
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
	 * 读取账户文件中的账号,保存到列表和数组中去
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
			MyLogger.fatal(getClass(), e.getMessage()+" 读取用户信息失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取用户的名称
	 * @param line 格式应该是 username=***&password=*****
	 * @return 返回用户的名称 username后面的部分
	 */
	private String getAccountName(String line)
	{
		String temp[]=line.split("&");
		int index=temp[0].indexOf("=");
		return temp[0].substring(index+1);
	}
	
	/**
	 * 更新用户列表中的数据,仅在内存中操作
	 * @param removeInt 更改的条目的序号
	 * @param newname 新的用户名
	 * @param newpassword 新的密码
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
	 * 删除用户,仅在内存中操作
	 * @param removeInt
	 * @return 返回被删除的用户名
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
	 * 添加新账户在列表的末尾
	 * 如果在数据表中已经有的,就更新数据
	 * 否则就在末尾添加
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
	 * 清空所有的数据
	 */
	public void clearAll() {
		usernameList.clear();
		accountMap.clear();
		generateArray(usernameList);
	}
	
	/**
	 * 将表单参数写入添加到文件中
	 * @param url表单的参数
	 */
	public void writeAccount(String params)
	{
		try {
			PrintWriter out = null;
			File f=new File(jarPath);//这句话并不会新建文件
			OutputStream in=new FileOutputStream(f,true);//让文件可以追加内容,而且这句话这里自动新建了一个
			out=new PrintWriter(in);
			out.append(Base64Coder.encode(params)+"\r\n");
			out.close();
			generateArray(usernameList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 更新文件中的数据,以及用户列表中的数据
	 * @param removeInt 移除的序号 
	 * @param name 新的名字
	 * @param password 新的密码
	 * @throws IOException 
	 */
	public void updateFile()
	{
		try {
			file = new File(decode(jarPath));
			if (file.exists())//删除和重新建立文件 清空里面的数据
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
	  将所给的文件路径进行编码,以识别包含中文或者是空格
	 * 的文件路径
	 * @param path 文件路径 可能包含空格和中文
	 * @return 转换后的路径,正确显示中文和空格
	 * @throws UnsupportedEncodingException
	 */
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}
	
	/**
	 * 把用户名称列表转换成字符串数组
	 * 保存在成员变量中
	 * @param list  用户名列表
	 */
	private void generateArray(ArrayList<String>list) {
		usernames = new String[list.size()];
		list.toArray(usernames);
	}
}
