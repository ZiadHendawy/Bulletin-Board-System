import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.jcraft.jsch.JSch;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


public class Start {

	/**
	 * @param args
	 */
	private static final String configFile = "system.properties";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/**
		 * parsing file
		 * */
		Parser parser = new Parser();
		HashMap <String, String> map =  parser.parse(configFile);
		start(map);
		
			     
	     
		
		
	}
	
	/**
	 * *
	 * this function start any java class by firstly compiling it  
	 * then running it
	 * used to start the server and the clients
	 * 
	 * @param path full path of desired class
	 * @param className name of desired class
	 */
	public static void start(HashMap <String, String> map){
		//setting server attribute 
		String server = map.get("RW.server");
		String serverPort = map.get("RW.server.port");
		String serverPW = map.get("RW.server.pw");
		
		//starting server
		String result = "";
		String [] ServerIPandUserName = split(server);
	    SSHManager serverInstance = new SSHManager( ServerIPandUserName[1], map.get(serverPW), ServerIPandUserName [0], "");
	    String serverErrorMessage = serverInstance.connect();
	     if(serverErrorMessage != null)
	    	 System.out.println(serverErrorMessage);
     	result = serverInstance.sendCommand("javac Client.java");
     	result = serverInstance.sendCommand("java Client "+" "+serverPort);
     	System.out.println(result);
     	serverInstance.close();

		
		
		
		
		//running server locally from same host of the this class 
		//String dir = "/home/hendawy/Desktop/src/";
		//String name = "Server";
		//run(dir, name);
		
		/**
		 * running clients
		 * */
		
		
	     //starting readers
	     for(int i = 0; i < Integer.parseInt(map.get("RW.numberOfReaders")); i++){
	    	 String [] IPandUserName = split(map.get("RW.reader"+i));
		     String password = map.get("RW.reader"+i+".pw");
		     SSHManager instance = new SSHManager( IPandUserName[1], password, IPandUserName [0], "");
		     String errorMessage = instance.connect();
	
		     if(errorMessage != null)
		    	 System.out.println(errorMessage);
		     
	     	result = instance.sendCommand("javac Client.java");
	     	result = instance.sendCommand("java Client "+ server +" "+serverPort);
	     	System.out.println(result);
	     	instance.close();
	     }	
	     
	     //starting writers
	     for(int i = 0; i < Integer.parseInt(map.get("RW.numberOfWriters")); i++){
	    	 String [] IPandUserName = split(map.get("RW.writer"+i));
		     String password = map.get("RW.writer"+i+".pw");
		     SSHManager instance = new SSHManager( IPandUserName[1], password, IPandUserName[0], "");
		     String errorMessage = instance.connect();
	
		     if(errorMessage != null)
		    	 System.out.println(errorMessage);
		     
		 
	     	result = instance.sendCommand("javac Client.java");
	     	result = instance.sendCommand("java Client "+ server +" "+serverPort);
	     	System.out.println(result);
	     	instance.close();
	     }

	}
	
	
	/**
	 *  this function used to compile and run any java class in the same host using java runtime 
	 *  @param dir file directory
	 *  @param className name of the class 
	 * */
	private static void run(String dir, String className){
		try {
			Process compileProcess = Runtime.getRuntime().exec
					("javac -cp \""+dir+"*\""+dir+className+".java");
        
			Process runProcess = Runtime.getRuntime().exec
					("java -cp "+dir+":"+dir+"* "+ className);
			BufferedReader input = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
			  
              String line=null;

              while((line=input.readLine()) != null) {
                  System.out.println(line);
              }
              //int exitVal = runProcess.waitFor();
              //System.out.println("Exited with error code "+exitVal);
              runProcess.destroy();
             
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static String [] split(String address){
		String [] IPandUserName = new String [2];
		if(address.indexOf('@') != -1){
			IPandUserName = address.split("@");
		}
		else{
			IPandUserName[0] = address;
			IPandUserName[1] = null;
		}
		return IPandUserName;
	}
	
	

}
