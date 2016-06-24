package readwrite;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TestCutNumberByLabel {

	private final String data = "<a properties>hello</a>";

	@Test
	public void test() throws IOException {
		int first = data.indexOf("<a properties>")
				+"<a properties>".length();
		int end = data.indexOf("</a>", first);
		assertEquals("hello", data.substring(first,end));
	}

}
