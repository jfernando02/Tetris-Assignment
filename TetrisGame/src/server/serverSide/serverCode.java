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

import server.clientSide.tetrisGameInfo;

public class serverCode {
    private static int activeClientCount = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server is running. Awaiting Connections from Clients. ");

            while (true) { // Handle Client Connections
                Socket clientSocket = serverSocket.accept();
                synchronized (serverCode.class) { // Synchronize to ensure thread safety
                    activeClientCount++;
                }
                String clientId = "Client " + activeClientCount;
                System.out.println("Client has connected, ID:" + activeClientCount);

                // Call new thread for each new client
                new Thread(new ClientHandler(clientSocket, clientId)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void clientDisconnected() {
        activeClientCount--;
    }
}

// Separate class to be able to have multiple External Clients connect
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String clientId;
    private static Random random = new Random(); // Just to create random values for rotation

    public ClientHandler(Socket socket, String clientId) {
        this.clientSocket = socket;
        this.clientId = clientId;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Process game info from the client
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(clientId + ": Message Received: " + message);
                String response = calculateMoveInfo(message);
                out.println(response);
            }
            System.out.println(clientId + " has disconnected");

        } catch (Exception e) {
            System.out.println("Error handling " + clientId + ": " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing client socket for " + clientId + ": " + e.getMessage());
            } finally {
                serverCode.clientDisconnected();
            }
        }
    }

    public static String calculateMoveInfo(String message) {
        Gson gson = new Gson();
        tetrisGameInfo gameData;

        try {
            gameData = gson.fromJson(message, tetrisGameInfo.class);
            // Retrieve Game Info
            int[][] cellsArray = gameData.parseCells();
            int[][] currentShapeArray = gameData.parseCurrentShape();
            int width = gameData.getWidth();
            int height = gameData.getHeight();

            // Logic for making better moves
            int[] columnHeights = calculateColumnHeights(cellsArray, width, height);
            int bestColumn = getBestColumn(columnHeights);
            int bestRotation = random.nextInt(4); // Currently still random

            return String.format("RotationCount: %d, xPosition: %d", bestRotation, bestColumn);

        } catch (JsonSyntaxException | JsonProcessingException e) {
            return "Error: Invalid JSON format";
        }
    }

    // Calculate column heights to know which column to favour
    public static int[] calculateColumnHeights(int[][] cellsArray, int width, int height) {
        int[] columnHeights = new int[width];

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (cellsArray[col][row] != 0) {
                    columnHeights[col] = height - row;
                    break;
                }
            }
        }
        return columnHeights;
    }

    // Find suitable column to place block
    public static int getBestColumn(int[] columnHeights) {
        int minHeight = Integer.MAX_VALUE;
        int bestColumn = 0;

        for (int col = 0; col < columnHeights.length; col++) {
            if (columnHeights[col] < minHeight) {
                minHeight = columnHeights[col];
                bestColumn = col;
            }
        }
        return bestColumn;
    }
}
