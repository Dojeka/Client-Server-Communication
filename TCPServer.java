import java.io.*;
import java.net.*;

public class TCPServer{
    public static void main(String[] args) throws IOException {
        // Variables for setting up connection and communication
        Socket socket = null; // socket to connect with ServerRouter
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading from ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        String host = addr.getHostAddress(); // Server machine's IP
        String routerName = "localhost"; // ServerRouter host name (use localhost if running on same machine)
        int SockNum = 5555; // port number

        // Tries to connect to the ServerRouter
        try {
            socket = new Socket(routerName, SockNum);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Variables for message passing
            String fromServer; // messages sent to ServerRouter
            String fromClient; // messages received from ServerRouter
            String address = "localhost"; // destination IP (Client)

            // Communication process (initial sends/receives)
            out.println(address); // initial send (IP of the destination Client)
            fromClient = in.readLine(); // initial receive from router (verification of connection)
            System.out.println("ServerRouter: " + fromClient);

            // Communication while loop
            while ((fromClient = in.readLine()) != null) {
                System.out.println("Client said: " + fromClient);
                if (fromClient.equals("Bye.")) { // exit statement
                    break;
                }
                // Convert to uppercase and send back
                fromServer = fromClient.toUpperCase(); // converting received message to upper case
                System.out.println("Server said: " + fromServer);
                out.println(fromServer); // sending the converted message back to the Client via ServerRouter
            }


        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerName);
            System.exit(1);
        }

        // closing connections
        out.close();
        in.close();
        socket.close();
    }
}
