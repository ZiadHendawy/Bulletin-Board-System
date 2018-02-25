import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private String serverAddress;
    private String portNumber;
    private String type;
    private String id;
    private int accesses;

    public Client(String serverAddress, String portNumber, String type,String id, int accesses){
    	System.out.println(serverAddress);
    	this.serverAddress = serverAddress;
        System.out.println(portNumber);
        this.portNumber = portNumber;
        this.type = type;
        this.id = id;
        this.accesses = accesses;
        log("log"+(Integer.parseInt(id)-1)+".txt","Client: "+type+"\n"+"Client Name: "+id+"\n", false);
    }

    



    public void connectToServer() throws IOException, InterruptedException {

        // Get the server address from a dialog box.
        
        // Make connection and initialize streams
    	Socket socket = null;
    	boolean header = true;
        String rSeq= "", sSeq = "", val = "";
		while(accesses > 0){
			socket = new Socket(serverAddress, Integer.parseInt(portNumber));
	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
	        
			if(type.equals("reader")){
        		if(header){
        			log("log"+(Integer.parseInt(id)-1)+".txt","rSeq sSeq oVal\n", true);
        			header = false;
        		}
        		out.println(type+" "+id+" ");
        	}
        	else{
        		if(header){
        			log("log"+(Integer.parseInt(id)-1)+".txt","rSeq sSeq\n", true);
        			header = false;
        		}
        		out.println(type+" "+id+" "+id);
        	}	
        	String response;
        	try {
        			
        		rSeq = in.readLine();
        		sSeq = in.readLine();
        		val = in.readLine();
        		if(type.equals("reader"))
        			log("log"+(Integer.parseInt(id)-1)+".txt",rSeq+"    "+sSeq+"    "+val+"\n", true);
        		else
        			log("log"+(Integer.parseInt(id)-1)+".txt",rSeq+"    "+sSeq+"    "+"\n", true);
        	} catch (IOException ex) {
        		response = "Error: " + ex;
        	}
        	//client simmulate real time of operations
        	int random = (int)(Math.random() * ((10000)+1));
    		Thread.sleep(random);
        	accesses--;
		}
        socket.close();
        
        
    }
    
    private  void log(String fileName, String text, boolean append) {
		BufferedWriter readWriter = null;
		BufferedWriter writeWriter = null;
			
		try {
			if(readWriter == null && type.equals("reader"))
				readWriter = new BufferedWriter(new FileWriter(fileName, append));
			if(writeWriter == null && type.equals("writer"))
				writeWriter = new BufferedWriter(new FileWriter(fileName, append));
			
			if(type.equals("writer")){
				writeWriter.write(text);
				writeWriter.close();
			}
			else if(type.equals("reader")){
				readWriter.write(text);
				readWriter.close();
				
			}
					
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
    }

    /**
     * Runs the client application.
     */
    
    public static void main(String[] args) throws Exception {
        try{
        	System.out.println("client args length:"+ args.length);
        	Client client = new Client(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));
        	client.connectToServer();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    
}
