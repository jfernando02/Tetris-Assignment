package server.clientSide;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class clientCode {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int portNum = 3000;
        Gson gson = new Gson();

        try (Socket socket = new Socket(serverAddress, portNum); // Socket Conection
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");
            String message;
            String serverResponse;

            // Testing Connection Messages with Random Values
            tetrisGameInfo game = new tetrisGameInfo(10, 20, new int[2][2], new int[4][4], new int[4][4]);

            while (true) {
                System.out.print("Enter a command (update OR exit): ");
                message = userInput.readLine();

                if (Objects.equals(message, "exit")) { // For Endgame
                    System.out.println("Leaving Server.");
                    out.println("Exit");
                    break;
                }

                if (Objects.equals(message, "update")) { // For Updates
                    // Here you could update game, for example:
                    game.getCells()[0][0] = 1; // Update some part of the game state
                    System.out.println("Game state updated.");
                }

                // Convert the TetrisGameInfo to a JSON string and send it to the server
                String gameJson = gson.toJson(game);
                out.println(gameJson);
                System.out.println("Game data sent to server: " + gameJson);

                // Check that server got the same message that was sent
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
