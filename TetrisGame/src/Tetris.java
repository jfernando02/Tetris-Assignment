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
import ui.panel.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tetris extends JFrame {
    public Tetris() {
        Game game = new Game();
        GamePanel gamePanel = new GamePanel(game);
        add(gamePanel);
        setupKeyBinding(game, "DOWN");
        setupKeyBinding(game, "LEFT");
        setupKeyBinding(game, "RIGHT");
        setupKeyBinding(game, "UP");
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.play();
                repaint();  // Optional: calls paintComponent() if you override it for drawing
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    private void setupKeyBinding(Game game, String direction) {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("pressed " + direction), "move" + direction);
        getRootPane().getActionMap()
                .put("move" + direction, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Key pressed: " + direction);
                        game.handleKeyPress(direction);
                    }
                });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame("Tetris Game", 800, 600);
            mainFrame.showSplashScreen();
            mainFrame.setVisible(true);
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Tetris tetris = new Tetris();
                tetris.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                tetris.setSize(400, 400);
                tetris.setVisible(true);
            }
        });
    }
}