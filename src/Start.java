
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

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
		System.out.println(server);
		System.out.println(serverPort);
		System.out.println(serverPW);
		//starting server
		String result = "";
		String [] serverIPandUserName = split(server);
	    SSHManager serverInstance = new SSHManager( serverIPandUserName[1], serverPW, serverIPandUserName [0], "");
	    String serverErrorMessage = serverInstance.connect();
	     if(serverErrorMessage != null)
	    	 System.out.println(serverErrorMessage);
     	result = serverInstance.sendCommand("javac Server.java");
     	result = serverInstance.sendCommand("java Server "+" "+serverPort + " > /dev/null 2>&1 &");
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
     	System.out.println(Integer.parseInt(map.get("RW.numberOfReaders")));
	     for(int i = 0; i < Integer.parseInt(map.get("RW.numberOfReaders")); i++){
	    	 String [] IPandUserName = split(map.get("RW.reader"+i));
		     String password = map.get("RW.reader"+i+".pw");
		     System.out.println(IPandUserName[1]);
		     System.out.println(password);
		     System.out.println(IPandUserName[0]);
		     SSHManager instance = new SSHManager( IPandUserName[1], password, IPandUserName [0], "");
		     String errorMessage = instance.connect();
	
		     if(errorMessage != null)
		    	 System.out.println(errorMessage);
		     
	     	result = instance.sendCommand("javac Client.java");
	     	result = instance.sendCommand("java Client "+ serverIPandUserName[0] +" "+serverPort+" reader "+(i+1)+" "+map.get("RW.numberOfAccesses")+" > /dev/null 2>&1 &");
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
	     	result = instance.sendCommand("java Client "+ serverIPandUserName[0] +" "+serverPort+" writer "+(i+1+Integer.parseInt(map.get("RW.numberOfReaders")))+" "+map.get("RW.numberOfAccesses")+" > /dev/null 2>&1 &");
	     	System.out.println(result);
	     	instance.close();
	     }

	}
	
	
	/**
	 *  this function used to compile and run any java class in the same host using java runtime 
	 *  use only if Start.java and Server.java are at the same host instead of using ssh
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
			IPandUserName[1] = address.split("@")[0];
			IPandUserName[0] = address.split("@")[1];
		}
		else{
			IPandUserName[0] = address;
			IPandUserName[1] = null;
		}
		return IPandUserName;
	}
	
	

}
