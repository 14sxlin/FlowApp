package base64;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Base64CoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String input = "Java���ܽ��ܵ�����";
		System.err.println("ԭ����: "+input);
		String code = Base64Coder.encode(input);
		System.err.println("�����: "+code);
		String output=  Base64Coder.decode(code);
		System.err.println("�����: "+ output);
		assertEquals(input, output);
		
	}

}
