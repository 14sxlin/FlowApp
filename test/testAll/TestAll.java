package testAll;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import readwrite.AccountManagerTest;
import readwrite.ConfigureTest;

@RunWith(Suite.class)
@SuiteClasses(value= {AccountManagerTest.class,ConfigureTest.class})
public class TestAll {

}
