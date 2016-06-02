package readwrite;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

public class Configure {
	
	public static String filePath ;
	
	public static void setFilePath(String filePath) {
		Configure.filePath = filePath;
	}
	
	//根据Key读取Value
    public static String GetValueByKey(String key) {
        Properties pps = new Properties();
        try {
            InputStream in = 
            		new BufferedInputStream (new FileInputStream(filePath));  
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
            
        }catch (IOException e) {
        	createDefaultFile();
			return GetValueByKey(key);
        }
    }
    
    //读取Properties的全部信息
    public static void GetAllProperties() throws IOException {
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        pps.load(in);
        Enumeration<?> en = pps.propertyNames(); //得到配置文件的名字
        
        while(en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            System.out.println(strKey + "=" + strValue);
        }
        
    }
    
    //写入Properties信息
    public static void WriteProperties (String pKey, String pValue) throws IOException {
        try {
			Properties pps = new Properties();
			InputStream in = new FileInputStream(filePath);
			//从输入流中读取属性列表（键和元素对） 
			pps.load(in);
			//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。  
			//强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream out = new FileOutputStream(filePath);
			pps.setProperty(pKey, pValue);
			//以适合使用 load 方法加载到 Properties 表中的格式，  
			//将此 Properties 表中的属性列表（键和元素对）写入输出流  
			pps.store(out, "Update " + pKey + " name");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			createDefaultFile();
			WriteProperties(pKey, pValue);
		}
    }
    private static void createDefaultFile() {
    	System.out.println("default ");
    	try {
			File file = new File(filePath);
			PrintWriter out = new PrintWriter(file);
			out.println("autoLogin=false\r\n" + 
					"autoSelect=false\r\n" + 
					"lastLogin=\r\n" + 
					"musicPath=\r\n" + 
					"defaultUser=");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
