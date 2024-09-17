package server.clientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class clientCode {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // or the server's IP address
        int portNum = 3000;

        try (Socket socket = new Socket(serverAddress, portNum);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");
            String message;
            String serverResponse;

            while (true) {
                System.out.println("Enter message to server: ");
                message = userInput.readLine();
                out.println(message);

                if (Objects.equals(message, "Exit")) {
                    System.out.println("Leaving Server.");
                    break;
                }

                serverResponse = in.readLine();
                System.out.println("From Server: " + serverResponse);
            }

            System.out.println("Connection to Server Has Closed.");
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
