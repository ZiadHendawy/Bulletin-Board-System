
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server program which accepts requests from clients to
 * capitalize strings.  When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent.  If you ran it from a console window with the "java"
 * interpreter, Ctrl+C generally will shut it down.
 */
public class Server {
	
    /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    private static String val;
    private static int seq = 0;
	private static boolean used = false;
	private static int rNum = 0;
    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        int clientNumber = 0;
        
        ServerSocket listener = null;
        val = "-1";
        //write headers of files
        log("readers.txt", "sSeq oVal rID rNum\n", false);
        log("writers.txt", "sSeq oVal wID\n", false);
        try{
        	 //9898
        	 listener = new ServerSocket(Integer.parseInt(args[0]));
        	
        	
        }
        catch(Exception e){
        	System.out.println(e.getMessage());
        }
        try {
        	
         	while (true) {
            	Socket socket = listener.accept();
            	//
            	
                new Handler(socket, clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static void log(String fileName, String text, boolean append) {
		BufferedWriter readWriter = null;
		BufferedWriter writeWriter = null;
			
		try {
			if(readWriter == null && fileName.equals("readers.txt"))
				readWriter = new BufferedWriter(new FileWriter(fileName, append));
			if(writeWriter == null && fileName.equals("writers.txt"))
				writeWriter = new BufferedWriter(new FileWriter(fileName, append));
			
			if(fileName.equals("writers.txt")){
				writeWriter.write(text);
				writeWriter.close();
			}
			else if(fileName.equals("readers.txt")){
				readWriter.write(text);
				readWriter.close();
				
			}
					
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
    }
    private static class Handler extends Thread {
        private Socket socket;
        private int clientNumber;
        
        public Handler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {
            	
                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                
            	BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                
                String input = in.readLine();
                String [] arr = input.split("\\s+");
                if(arr[0].equals("read")){
                	//write in reader log file
                	try{
                		rNum++;
                		log("readers.txt", " "+seq+"   "+val+"   "+arr[1]+"   "+rNum+"\n", true);
                	}catch(Exception e){
                		System.out.println("invalid write request");
                	}	
                	
                }
                else if(arr[0].equals("write")){
                	//write in writer log file
                	
                	try{
                		log("writers.txt", " "+seq+"   "+arr[1]+"   "+arr[1]+"\n", true);
                		
                		while(used);
                		used = true;
                		val = arr[2];
                		used = false;
                	}catch(Exception e){
                		System.out.println("invalid write request");
                	}		
                }
                while(used); 	
                used = true;
                seq++;
                used = false;
                out.println(val);
                
                	
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                	used = false;
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        
        
        private boolean isReader(){
        	return true;
        }
        
        private boolean isWriter(){
        	return true;
        }
        
        
    }
}
