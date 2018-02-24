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

    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Enter in the
     * listener sends the textfield contents to the server.
     */
    public Client(String serverAddress, String portNumber) {

        this.serverAddress = serverAddress;
        this.portNumber = portNumber;

    }

    /**
     * Implements the connection logic by prompting the end user for
     * the server's IP address, connecting, setting up streams, and
     * consuming the welcome messages from the server.  The Capitalizer
     * protocol says that the server sends three lines of text to the
     * client immediately after establishing a connection.
     */
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        
        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, Integer.parseInt(portNumber));
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

		out.println("Client alive");
        String response;
        try {
          response = in.readLine();
          if (response == null || response.equals("")) {
                System.exit(0);
            }
        } 
        catch (IOException ex) {
             response = "Error: " + ex;
        }
        
        
        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
	    //System.out.println(in.readLine());
        	in.readLine();
        }
    }

    /**
     * Runs the client application.
     */
    public static void main(String[] args) throws Exception {
        try{
        	Client client = new Client(args[0], args[1]);
        	client.connectToServer();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        }
}
