package server.serverSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverCode {
    public static void main(String[] args) {
        System.out.println("Server started.");
        int portNum = 3000;
        int X = 5; // Example Value
        int rotationCount = 3; // Example Value

        try (ServerSocket serverSocket = new ServerSocket(portNum);
             Socket socket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            System.out.println("Client Has Connected.");
            String jsonMessage;
            while ((jsonMessage = in.readLine()) != null) {
                if (jsonMessage.equals("EXIT")) {
                    System.out.println("Client has Disconnected.");
                    out.println("Server has shut down at your request.");
                    break;
                }

                System.out.println("Client Message: " + jsonMessage); // Message that Server sees
                out.println("X position = " + X + ", rotationCount = " + rotationCount); // Example moves for tetrisClient to make in-games

            }
        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

