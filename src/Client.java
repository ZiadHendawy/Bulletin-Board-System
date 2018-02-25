import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * A simple Swing-based client for the capitalization server.
 * It has a main frame window with a text field for entering
 * strings and a textarea to see the results of capitalizing
 * them.
 */
public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private String serverAddress;
    private String portNumber;
    private String type;
    private String id;
    private int accesses;
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Enter in the
     * listener sends the textfield contents to the server.
     */
    public Client(String serverAddress, String portNumber, String type,String id, int accesses){
    	System.out.println(serverAddress);
    	this.serverAddress = serverAddress;
        System.out.println(portNumber);
        this.portNumber = portNumber;
        this.type = type;
        this.id = id;
        this.accesses = accesses;
    }

    

	/**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The Capitalizer
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
    public void setAccesses(int accesses){
    	this.accesses = accesses;
    }
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        
        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, Integer.parseInt(portNumber));
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

		if(type.equals("read"))
        	out.println(type+" "+id+" ");
		else
			out.println(type+" "+id+" "+1);
		String response;
        try {
          response = in.readLine();
         } catch (IOException ex) {
             response = "Error: " + ex;
        }
        
        
        
    }

    /**
     * Runs the client application.
     */
    
    public static void main(String[] args) throws Exception {
        try{
        	Client client = new Client(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));
        	client.connectToServer();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    
}
