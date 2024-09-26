package server.serverSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Arrays;
import server.clientSide.tetrisGameInfo;

public class serverCode {
    private static final Random random = new Random(); // Just to create random values for rotation/x position

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server is running...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) { // Accept Connection with Client
                    System.out.println("Client connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // Wait for Message from Client and then calculate what move to make and then send it back
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Message Received: " + message);
                        String response = calculateMoveInfo(message);
                        out.println(response);
                    }

                    System.out.println("Client has disconnected");
                } catch (Exception e) {
                    System.out.println("Error for Client/Server Connection: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String calculateMoveInfo(String message) {
        Gson gson = new Gson();
        tetrisGameInfo gameData;

        try {
            gameData = gson.fromJson(message, tetrisGameInfo.class);

            // Still need to update the deciding logic to incorporate some sort of algorithm to determine rotation/xPosition.
            try {
                int[][] cellsArray = gameData.parseCells();
                int[][] nextShapeArray = gameData.parseNextShape();

                // Random Values For Now
                int rotationCount = random.nextInt(4);
                int xPosition = random.nextInt(gameData.getWidth());

                return String.format("RotationCount: %d, xPosition: %d", rotationCount, xPosition); // Format to send message back to client
            } catch (JsonProcessingException e) {
                return "Error: Failed to convert arrays.";
            }

        } catch (JsonSyntaxException e) {
            return "Error: Invalid JSON format";
        }
    }
}