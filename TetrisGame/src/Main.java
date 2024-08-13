// Our entry point to the application which will handle the game loop and other game related logic

/* Structure of the project:
TetrisGame/
├── src/
│   ├── main/
│   │   └── Main.java // Entry point to the application which will handle the game loop and other game related logic
│   ├── model/
│   │   ├── Game.java // Manages game state, updates, and overall control
│   │   ├── Score.java // Handles scoring logic
│   │   ├── TetrisShape.java // Defines the shapes and their rotations
│   │   └── TetrisShapeInstance.java // Represents a specific instance of a Tetris shape in the game
│   ├── resources/
│   │   └── images/ // Stores image resources like Tetris block images
│   ├── ui/
│   │   ├── field/
│   │   │   └── FieldPane.java // Panel that displays the game field (interactive area of game)
│   │   ├── panel/
│   │   │   ├── ConfigurePanel.java // Allows user configuration or settings
│   │   │   ├── GamePanel.java // Displays the main game interface
│   │   │   ├── HighScorePanel.java // Shows high scores
│   │   │   ├── MainPanel.java // The main panel that might include menus or game start options
│   │   │   └── PlayPanel.java // Specific to gameplay, probably includes controls and active game display
│   │   ├── MainFrame.java // The main application window/frame
│   │   ├── SplashPanel.java // The initial screen shown when the game starts
│   │   └── UIGenerator.java // Helper class for generating UI components
│   └── util/
│       └── CommFun.java // Contains common utility functions used throughout the application
 */

import model.Game;
import ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame("Tetris Game", 800, 600);
            mainFrame.showSplashScreen();
            mainFrame.setVisible(true);
        });

        Game game = new Game();
        // Game thread (outside of main screen thread)
        new Thread(() -> {
            while (true) {
                try {
                    game.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}