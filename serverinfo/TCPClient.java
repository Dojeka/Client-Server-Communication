package serverinfo;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        // Establish a connection to the router/server
        Socket routerSocket = new Socket("localhost", 1234);
        PrintWriter out = new PrintWriter(routerSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(routerSocket.getInputStream()));

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);
        
        // Variables for storing messages
        String fromUser; // For user input
        String fromServer; // For server response

        // Communication loop
        while (true) {
            System.out.print("Enter message (type 'Bye.' to exit): ");
            fromUser = scanner.nextLine(); // reading strings from user input
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser); // sending the strings to the Server via ServerRouter
            }

            fromServer = in.readLine(); // Read response from the server
            if (fromServer == null || fromServer.equals("Bye.")) { // exit if the server sends "Bye."
                System.out.println("Server: " + fromServer);
                break;
            }
            
            System.out.println("Server: " + fromServer); // Print server response
        }

        // closing connections
        out.close();
        in.close();
        routerSocket.close(); // use routerSocket to close the socket connection
        scanner.close(); // Close the scanner object
    }
}
