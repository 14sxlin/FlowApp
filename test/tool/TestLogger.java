package tool;

import org.junit.Test;

public class TestLogger {

	@Test
	public void test() {
		MyLogger.setLogger(this.getClass());
		MyLogger.info("hello");
	}

}
