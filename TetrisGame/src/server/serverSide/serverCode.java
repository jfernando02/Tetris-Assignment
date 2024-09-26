package server.serverSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
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

            while (true) { // Initalise all data connections to be able to close everything before new instance of a game
                Socket clientSocket = null;
                BufferedReader in = null;
                PrintWriter out = null;

                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("Client connected");

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // Process as normal
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Message Received: " + message);
                        String response = calculateMoveInfo(message);
                        out.println(response); // Send response to client
                    }

                    System.out.println("Client has disconnected");

                } catch (Exception e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally { // close resources after complete
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (clientSocket != null && !clientSocket.isClosed()) {
                            clientSocket.close();
                        }
                    } catch (Exception e) {
                        System.out.println("Error closing resources: " + e.getMessage());
                    }
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