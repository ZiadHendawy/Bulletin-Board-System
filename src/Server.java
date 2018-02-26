
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
    private static String val;
	private static boolean used = false;
	private static int rNum = 0;
	private static int rSeq = 0;
	private static int sSeq = 0;
	private static boolean criticalSection1 = false;
	private static boolean criticalSection2 = false;
	private static boolean criticalSection3 = false;
	private static boolean criticalSection4 = false;
	private static boolean criticalSection5 = false;
	
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
                new Handler(socket, clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

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

        public void run() {
        	String [] arr = null;
        	
        	try {
            	                
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String input = in.readLine();
                
                //critical section for global variable rSeq
                while(criticalSection1)
                	Thread.sleep(1000);
                criticalSection1 = true;
                out.println(""+(rSeq++));
                criticalSection1 = false;
                
                //wait random time 1 ---> 10,000
                int random = (int)(Math.random() * ((10000)+1));
        		Thread.sleep(random);
        		
        		arr = input.split("\\s+");
                if(arr[0].equals("reader")){
                	//write in reader log file
                	try{
                        //critical section for global variable rNum
                		while(criticalSection2)
                			Thread.sleep(1000);
                		criticalSection2 = true;
                		rNum++;
                		log("readers.txt", " "+sSeq+"   "+val+"   "+arr[1]+"   "+rNum+"\n", true);
                		criticalSection2 = false;
                	}catch(Exception e){
                		System.out.println("invalid write request");
                	}	
                	
                }
                else if(arr[0].equals("writer")){
                	//write in writer log file
                	
                	try{
                        //critical section for global variable val
                		while(criticalSection3)
                			Thread.sleep(1000);
                		criticalSection3 = true;
                		val = arr[2];
                		log("writers.txt", " "+sSeq+"   "+arr[1]+"   "+arr[1]+"\n", true);
                		criticalSection3 = false;
                	}catch(Exception e){
                		System.out.println("invalid write request");
                	}		
                }
                
                //critical section for global variable sSeq
                while(criticalSection4)
                	Thread.sleep(1000);
                criticalSection4 = true;
                sSeq++;
                criticalSection4 = false;
                out.println(""+sSeq);
                out.println(val);
                
                	
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                	
                	if(arr[0].equals("reader")){
                        //critical section for global variable rSeq
                		while(criticalSection5)
                			Thread.sleep(1000);
                		criticalSection5 = true;
                		rNum--;
                		criticalSection5 = false;
                	}
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }

        
        
        
    }
}
