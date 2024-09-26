package controller;

import com.google.gson.Gson;
import model.gamefactory.Game;
import server.clientSide.tetrisGameInfo;
import model.TetrisBlock;
import model.gamefactory.GameDefault;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ExternalController {
    private MainFrame mainFrame;
    private TetrisBlock tetrisBlock;
    private GameDefault game;
    private PrintWriter out; // To send messages to the server
    private Socket socket;

    private int rotationCount; // Store rotation count
    private int xPosition;      // Store x position

    public ExternalController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setGame(Game game) {
        this.game = (GameDefault) game;
    }

    public ExternalController(MainFrame mainFrame, Game game) {
        this.mainFrame = mainFrame;
        this.tetrisBlock = new TetrisBlock();
        this.game = (GameDefault) game;
    }

    public void run() {
        String serverAddress = "localhost";
        int portNum = 3000;
        System.out.println("External Controller Says: Client has attempted to connect to server.");
        try {
            socket = new Socket(serverAddress, portNum); // Socket Connection
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("External Controller Says: Client has connected to server.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> { // Safer Method of Receiving Messages from the Server.
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
//                        System.out.println("External Controller Says: Server - " + serverMessage);
                        System.out.println("External Controller Says: MoveInfo Received");
                        handleServerResponse(serverMessage); // Handle the server response
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            System.out.println("External Controller Says: Error Type - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void sendGameUpdate() {
        if (mainFrame == null || game == null) { // Debug Check
            System.err.println("External Controller Says: mainFrame/game is not initialized.");
            return;
        }

        if (socket == null || socket.isClosed()) { // Debug Check
            System.out.println("External Controller Says: Socket not connected. Will Establish Connection inside sendGameUpdate().");
            run();  // Establish the socket connection if there is none
        }

        if (out == null) { // Debug Check
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("External Controller Says: Could not initialize PrintWriter.");
                e.printStackTrace();
                return;
            }
        }

        // Information To Send to Server.
        int width = mainFrame.getConfigData().getFieldWidth();
        int height = mainFrame.getConfigData().getFieldHeight();
        TetrisBlock currentPiece = game.getActiveShape();
        if (currentPiece == null) { // Debug Check
            System.out.println("Current Piece is NULL");
            return; // Exit if there's no active piece
        }
        TetrisBlock nextPiece = game.getNextPiece();
        String board = Arrays.deepToString(game.getBoard().convertBoard());
        String nextShape = "Empty"; // incase of null
        String currentShape = "Empty"; // incase of null

        Gson gson = new Gson();

        if (currentPiece != null) {
            nextShape = Arrays.deepToString(nextPiece.getShape());
            currentShape = Arrays.deepToString(currentPiece.getShape());
        } else if (nextPiece != null) {
            nextShape = Arrays.deepToString(nextPiece.getShape());
        } else {
            System.out.println("Current Piece is NULL");
        }

        tetrisGameInfo gameInfo = new tetrisGameInfo(width, height, board, currentShape, nextShape);
        String gameJson = gson.toJson(gameInfo);
        out.println(gameJson);
        System.out.println("External Controller Says: Game State sent to Server.");
    }

    private void handleServerResponse(String message) {
        String[] parts = message.split(", ");
        try {
            for (String part : parts) {
                String[] keyValue = part.split(": ");
                if ("RotationCount".equals(keyValue[0])) {
                    rotationCount = Integer.parseInt(keyValue[1]);
                } else if ("xPosition".equals(keyValue[0])) {
                    xPosition = Integer.parseInt(keyValue[1]);
                }
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing server response: " + e.getMessage());
        }
    }

    // Getters for call inside GameExternal
    public int getRotationCount() {
        return rotationCount;
    }

    public int getXPosition() {
        return xPosition;
    }

}
