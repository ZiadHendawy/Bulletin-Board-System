import static org.junit.Assert.*;

import org.junit.Test;


public class SSHManagerTest {
	/**
	 * testing are made by creating different users and try to compile/run java classess from other user using JSch which is  
	 * a library imported in project libraries
	 * SHHManager handles all JSch usage
	 * */
	@Test
	public void SSHMangerSmokeTest() {
	     assertEquals("hello from hendawy user\n", testHelper(null, "localhost", "123456", "5555", ""));
	     assertEquals("hello from hendawy user\n", testHelper("hendawy", "localhost", "123456", "5555", "localhost 555 reader 1 1"));
	     assertEquals("hello from ziad user\n", testHelper("ziad", "localhost", "123456", "5555", "localhost 555 reader 1 1"));
	     	     
	     
	     
	     
	  }
	public String testHelper(String userName, String connectionIP, String password, String fileName, String param){
		SSHManager instance = new SSHManager(userName, password, connectionIP, "");
	     String errorMessage = instance.connect();

	     if(errorMessage != null)
	     {
	        System.out.println(errorMessage);
	        fail();
	     }

	    
	     String result = instance.sendCommand("pwd");
	     result = instance.sendCommand("javac "+fileName+".java");
	     result = instance.sendCommand("java "+fileName+" "+param+" > /dev/null 2>&1 &");
	     //result = instance.sendCommand("java hello "+ serverPort);
	     instance.close();
	     return  result;
	     

	}
	
	@Test
	//before running this test change hello class in ziad to have infinite loop after printing
	public void ClientUserServerUserWriter(){
		
		testHelper("server", "localhost", "123456", "Server", "8888");
		testHelper("client", "localhost", "123456", "Client", "localhost 8888 writer 1 1");
	}
	
	//
	@Test
	public void ClientUserServerUserReader(){
		
		testHelper("server", "localhost", "123456", "Server", "8888");
		testHelper("client", "localhost", "123456", "Client", "localhost 8888 reader 1 1");
	}

}
