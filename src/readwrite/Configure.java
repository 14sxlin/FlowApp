package readwrite;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import tool.MyLogger;

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
				if(value==null) {
					createDefaultFile();
					GetValueByKey(key);
				}
				in.close();
				return value;
			} catch (FileNotFoundException e) {
				MyLogger.setLogger(Configure.class);
				MyLogger.info(e.getMessage());
				createDefaultFile();
				MyLogger.info("����Ĭ���ļ�");
			} catch (IOException e) {
				e.printStackTrace();
				MyLogger.fatal(e.getMessage());
			}
			return null;
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
    
    /**
     * ����Properties��Ϣ
     * @param pKey key
     * @param pValue �µ�ֵ
     * @throws IOException
     */
    public static void WriteProperties (String pKey, String pValue) throws IOException {
    	InputStream in  = null;
    	OutputStream out = null;
    	Properties pps = new Properties();
		in = new FileInputStream(filePath);
		//���������ж�ȡ�����б�����Ԫ�ضԣ� 
		pps.load(in);
		//���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�  
		//ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
		out = new FileOutputStream(filePath);
		pps.setProperty(pKey, pValue);
		//���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��  
		//���� Properties ���е������б�����Ԫ�ضԣ�д�������  
		pps.store(out, "Update " + pKey + " name");
		in.close();
		out.close();
    }
    private static void createDefaultFile(OutputStream outs) {
    	PrintWriter out = new PrintWriter(outs);
		out.println(
				"# load default config\r\n"+
				"autoLogin=false\r\n" + 
				"autoSelect=false\r\n" + 
				"lastLogin=\r\n" + 
				"musicPath=\r\n" + 
				"defaultUser=");//д��Ĭ�ϵ�����
		out.flush();
		out.close();
    }
    /**
     * ��Ĭ�ϵ�����д���ļ�����ȥ
     */
    public static void createDefaultFile() {
    	OutputStream out = null;
    	try {
			out = new FileOutputStream(filePath);
			createDefaultFile(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			MyLogger.setLogger(Configure.class);
			MyLogger.fatal("�����ļ��޷����ʻ��Ҳ���");
		}
    }
}
