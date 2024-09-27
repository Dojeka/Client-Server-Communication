import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        // Configuration Parameters
        String routerName = "j263-08.cse1.spsu.edu"; // ServerRouter host name
        int routerPort = 5555; // ServerRouter port number
        String clientAddress = "10.5.3.196"; // Destination Client IP

        Socket routerSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            // Obtain Server's IP Address
            InetAddress serverInetAddress = InetAddress.getLocalHost();
            String serverIP = serverInetAddress.getHostAddress();
            System.out.println("Server IP Address: " + serverIP);

            // Establish Connection to ServerRouter
            routerSocket = new Socket(routerName, routerPort);
            System.out.println("Connected to ServerRouter at " + routerName + ":" + routerPort);

            // Initialize I/O Streams
            out = new PrintWriter(routerSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(routerSocket.getInputStream()));

            // Initial Communication with ServerRouter
            // 1. Send Client's IP Address to Router
            out.println(clientAddress);
            System.out.println("Sent Client IP to ServerRouter: " + clientAddress);

            // 2. Receive Verification from Router
            String verificationMessage = in.readLine();
            if (verificationMessage == null) {
                System.err.println("ServerRouter closed the connection unexpectedly.");
                return;
            }
            System.out.println("ServerRouter: " + verificationMessage);

            // Communication Loop
            String messageFromClient;
            while ((messageFromClient = in.readLine()) != null) {
                System.out.println("Client said: " + messageFromClient);

                // Check for Termination Signal
                if (messageFromClient.equalsIgnoreCase("Bye.")) {
                    System.out.println("Termination signal received. Closing connection.");
                    out.println("Bye."); // Optionally, send termination acknowledgment
                    break;
                }

                // Process the Message (Convert to Uppercase)
                String processedMessage = messageFromClient.toUpperCase();
                System.out.println("Server said: " + processedMessage);

                // Send the Processed Message Back to Client via Router
                out.println(processedMessage);
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown ServerRouter host: " + routerName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error occurred while communicating with ServerRouter.");
            e.printStackTrace();
        } finally {
            // Close Resources
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (routerSocket != null && !routerSocket.isClosed()) routerSocket.close();
                System.out.println("Server connections closed.");
            } catch (IOException e) {
                System.err.println("Error while closing connections.");
                e.printStackTrace();
            }
        }
    }
}
