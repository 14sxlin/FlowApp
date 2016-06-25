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
		String input = "Java加密解密的艺术";
		System.err.println("原文是: "+input);
		String code = Base64Coder.encode(input);
		System.err.println("编码后: "+code);
		String output=  Base64Coder.decode(code);
		System.err.println("解码后: "+ output);
		assertEquals(input, output);
		
	}

}
