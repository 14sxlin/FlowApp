package readwrite;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ConfigureTest {
	private String path = "config.properties";
	@Before
	public void setup() {
		Configure.setFilePath(path);
	}
	
	@Test
	public void testSetGetValue() throws IOException {
		Configure.WriteProperties("autoLogin", "true");
		Configure.WriteProperties("musicPath", "asdfgh");
		Configure.WriteProperties("lastLogin", "14sxlin");
		
		assertEquals("true", Configure.GetValueByKey("autoLogin"));
		assertEquals("asdfgh", Configure.GetValueByKey("musicPath"));
		assertEquals("14sxlin", Configure.GetValueByKey("lastLogin"));
	}

}
