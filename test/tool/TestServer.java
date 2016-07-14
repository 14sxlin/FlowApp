package tool;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestServer {

	@Test
	public void test() {
		String r = "";
		System.out.println(r+=new Server().getServerPath());
		assertEquals("http://192.168.9.30:8080", r);
	}

}
