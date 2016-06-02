package lin.readwrite;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.junit.Test;


public class TestDecode {
	//���Կո�������ܲ���������ʾ
	final String path="D:/program files/�������";
	URL url;
	private class MockResourcePath implements ResourcePath
	{
		public MockResourcePath() throws MalformedURLException {
			// TODO Auto-generated constructor stub
			url=new URL("D:/program files/�������");
		}
		@Override
		public String decode(String path) throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			return URLDecoder.decode(
					url.getPath(), "utf-8");
		}
		
	}
	@Test(expected=MalformedURLException.class)
	public void testDecoderWorkOK() throws UnsupportedEncodingException, MalformedURLException {
		new MockResourcePath();
		System.out.println(url.getPath());
		assertEquals(path, new MockResourcePath().decode(path));
		assertNotEquals(url.toString(), new MockResourcePath().decode(path));
	}

}
