import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.JSch;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dir = "/home/hendawy/Desktop/src/";
		String name = "Server";
		run(dir, name);
		
		
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
	public static void run(String dir, String className){
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
              int exitVal = runProcess.waitFor();
              System.out.println("Exited with error code "+exitVal);
              //runProcess.destroy();
             
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
