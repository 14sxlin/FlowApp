package readwrite;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import readwrite.AccountManager;

public class AccountManagerTest {

	private static File file;
	private static AccountManager am;	
	@BeforeClass
	public static void setup() throws IOException {
		 file  = new File("temp.txt");
		 am = new AccountManager(file.getAbsolutePath());
		 for(int i = 0;i<10;i++)
		{
			am.add(""+i, ""+i);
			am.updateFile();
		}
	}
	@Test
	public void testAddAccount() {
		for(int i = 0;i<10;i++)
		{
			am.add(""+i, ""+i);
			am.updateFile();
		}
//		for(String item:am.usernameList)
//			System.out.println(item);
//		System.out.println("------------------");
		assertEquals(10, am.accountMap.size());
		assertEquals(10, am.usernameList.size());
		assertEquals(10, am.usernames.length);
	}
	@Test
	public void testAddDuplication() {
		am.add("2", "doubi");
		am.updateFile();
		am.loadAccounts();
//		for(String item:am.usernameList)
//			System.out.println(item);
//		System.out.println("------------------");
		assertEquals("AuthenticateUser=2&AuthenticatePassword=doubi&shit", am.accountMap.get("2"));
		assertEquals(10, am.usernames.length);
	}
	@Test
	public void testDrop() {
		String deleted = am.drop(2);
		assertEquals(null, am.accountMap.get(deleted));
		assertEquals(9, am.accountMap.size());
		assertEquals(am.accountMap.size(), am.usernameList.size());
		assertEquals(9, am.usernames.length);
	}
	
	@Test
	public void testClearAll() {
		am.clearAll();
		am.updateFile();
		assertEquals(0, am.accountMap.size());
		assertEquals(am.accountMap.size(), am.usernameList.size());
	}
	
	@AfterClass
	public static void deleteFile()
	{
		file.delete();
	}
	
}
