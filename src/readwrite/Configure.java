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
	
	//����Key��ȡValue
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
    
    //��ȡProperties��ȫ����Ϣ
    public static void GetAllProperties() throws IOException {
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        pps.load(in);
        Enumeration<?> en = pps.propertyNames(); //�õ������ļ�������
        
        while(en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            System.out.println(strKey + "=" + strValue);
        }
        
    }
    
    //д��Properties��Ϣ
    public static void WriteProperties (String pKey, String pValue) throws IOException {
        try {
			Properties pps = new Properties();
			InputStream in = new FileInputStream(filePath);
			//���������ж�ȡ�����б�����Ԫ�ضԣ� 
			pps.load(in);
			//���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�  
			//ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
			OutputStream out = new FileOutputStream(filePath);
			pps.setProperty(pKey, pValue);
			//���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��  
			//���� Properties ���е������б�����Ԫ�ضԣ�д�������  
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
