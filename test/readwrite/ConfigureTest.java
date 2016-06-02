package readwrite;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ConfigureTest {
	private String path = 
			this.getClass().getClassLoader()
					.getResource("config.properties")
					.toString().substring(6);
	@Before
	public void setup() {
		Configure.setFilePath(path);
	}
	
	
	@Test
	public void testGetValue() throws IOException {
		assertEquals("false", Configure.GetValueByKey("autoSelect"));
		assertEquals("", Configure.GetValueByKey("musicPath"));
	}
	@Test
	public void testSetValue() throws IOException {
		Configure.WriteProperties("autoLogin", "true");
		assertEquals("true", Configure.GetValueByKey("autoLogin"));
		Configure.WriteProperties("autoLogin", "false");
		assertEquals("false", Configure.GetValueByKey("autoLogin"));
	}

}
