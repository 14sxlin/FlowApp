package tool;

import org.junit.Test;

import readwrite.UseInfo;

public class TestUsrInfo {
	@Test
	public void testRefresh() throws Exception {
		UseInfo.Refresh();
		System.out.println("TestUsrInfo.testRefresh():  "
				+UseInfo.userName+" "
				+UseInfo.used+" "
				+UseInfo.total+" "
				+UseInfo.remain);
	}

}
