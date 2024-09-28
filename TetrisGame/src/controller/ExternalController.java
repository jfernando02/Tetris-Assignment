package controller;

import com.google.gson.Gson;
import model.gamefactory.Game;
import server.clientSide.tetrisGameInfo;
import model.TetrisBlock;
import model.gamefactory.GameDefault;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ExternalController {
    private MainFrame mainFrame;
    private GameDefault game;
    private PrintWriter out; //
    private Socket socket;

    private int rotationCount;
    private int xPosition;

    public ExternalController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setGame(Game game) {
        this.game = (GameDefault) game;
    }

    // Main Code that establishes connection to server when available and reading server message
    public void run() {
        String serverAddress = "localhost";
        int portNum = 3000;
        try {
            socket = new Socket(serverAddress, portNum);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("External Controller Says: Client has now connected to server.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> { // For receiving move info from server
                try {
                    String serverMessage;
                    System.out.println("External Controller Says: Server/Client Information Exchange has begun");
                    while ((serverMessage = in.readLine()) != null) {
                        handleServerResponse(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
//            System.out.println("External Controller Says: Error Type - " + e.getMessage());
        }
    }

    // Code to get information of the game and send it to the server
    public synchronized void sendGameUpdate() {
        // Information To Send to Server.
        int width = mainFrame.getConfigData().getFieldWidth();
        int height = mainFrame.getConfigData().getFieldHeight();
        TetrisBlock currentPiece = game.getActiveShape();
        TetrisBlock nextPiece = game.getNextPiece();
        String board = Arrays.deepToString(game.getBoard().convertBoard());
        String nextShape = Arrays.deepToString(nextPiece.getShape());
        String currentShape = Arrays.deepToString(currentPiece.getShape());
        Gson gson = new Gson();

        tetrisGameInfo gameInfo = new tetrisGameInfo(width, height, board, currentShape, nextShape);
        String gameJson = gson.toJson(gameInfo);
        out.println(gameJson);
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

    // Getters for calls inside GameExternal
    public int getRotationCount() {return rotationCount;}

    public int getXPosition() {return xPosition;}

    // for GameExternal so clients can connect after starting a new game
    public void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("External Controller Says: Disconnected from server.");
            }
        } catch (IOException e) {
            System.err.println("External Controller Says: Error while disconnecting - " + e.getMessage());
        }
    }

    // Just to check whether the server/client connection was properly established
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void showServerConnectionError() {
        JDialog dialog = new JDialog(mainFrame, "Server Connection Error", Dialog.ModalityType.MODELESS);

        // Set up message, positioning, etc.
        JLabel messageLabel = new JLabel("<html><body>Warning: Server isn't currently active");
        messageLabel.setForeground(Color.BLACK);
        dialog.add(messageLabel, BorderLayout.CENTER);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        dialog.setLayout(new BorderLayout());
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Setup Dialog window
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        // Timer
        new javax.swing.Timer(1500, e -> dialog.dispose()).start();
    }
}
