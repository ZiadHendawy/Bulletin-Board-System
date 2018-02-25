import static org.junit.Assert.*;

import org.junit.Test;


public class SSHManagerTest {

//	@Test
	public void SSHMangerSmokeTest() {
	     assertEquals("hello from hendawy user\n", testHelper(null, "localhost", "123456"));
	     assertEquals("hello from hendawy user\n", testHelper("hendawy", "localhost", "123456"));
	     assertEquals("hello from ziad user\n", testHelper("ziad", "localhost", "123456"));
	     	     
	     
	     
	     
	  }
	public String testHelper(String userName, String connectionIP, String password){
		SSHManager instance = new SSHManager(userName, password, connectionIP, "");
	     String errorMessage = instance.connect();

	     if(errorMessage != null)
	     {
	        System.out.println(errorMessage);
	        fail();
	     }

	     String serverPort = "9898";
	     String result = instance.sendCommand("pwd");
	     result = instance.sendCommand("javac hello.java");
	     result = instance.sendCommand("java hello "+ serverPort + " > /dev/null 2>&1 &");
	     //result = instance.sendCommand("java hello "+ serverPort);
	     instance.close();
	     return  result;
	     

	}
	
	@Test
	//before running this test change hello class in ziad to have infinite loop after printing
	public void SSHManagerInfintLoop(){
		
		assertEquals("hello form ziad user\n", testHelper("ziad", "localhost", "123456"));
	}

}
