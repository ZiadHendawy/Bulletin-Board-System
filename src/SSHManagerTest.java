import static org.junit.Assert.*;

import org.junit.Test;


public class SSHManagerTest {

	@Test
	public void test() {
		
		 System.out.println("sendCommand");

	     /**
	      * YOU MUST CHANGE THE FOLLOWING
	      * FILE_NAME: A FILE IN THE DIRECTORY
	      * USER: LOGIN USER NAME
	      * PASSWORD: PASSWORD FOR THAT USER
	      * HOST: IP ADDRESS OF THE SSH SERVER
	     **/
	     String command = "";
	     String userName = "ziad";
	     String password = "123456";
	     String connectionIP = "localhost";
	     SSHManager instance = new SSHManager(userName, password, connectionIP, "");
	     String errorMessage = instance.connect();

	     if(errorMessage != null)
	     {
	        System.out.println(errorMessage);
	        fail();
	     }

	     String expResult = "sdfkksldf\n";
	     // call sendCommand for each command and the output 
	     //(without prompts) is returned
	     //String result = instance.sendCommand(command);
	    
	     String result = instance.sendCommand("pwd");
	     result = instance.sendCommand("javac hello.java");
	     result = instance.sendCommand("java hello");
	     System.out.println(result);
	     //result = instance.sendCommand("java Hello");
	     //System.out.println(result);
	     
	     // close only after all commands are sent
	     instance.close();
	     assertEquals(expResult, result);
	  }

}
