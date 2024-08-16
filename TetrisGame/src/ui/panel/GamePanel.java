// Displays the main game interface
package ui.panel;

//class holds the field pane
import model.Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    Game game;
    Color[] colors = {Color.WHITE, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.CYAN};

    public GamePanel(Game game) {
        this.game=game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw game state here
        // Example:
        // g.setColor(Color.BLACK);
        // g.drawOval(gameState.ballPosition.x, gameState.ballPosition.y, 20, 20);
        for (int j = 0; j < game.getHeight(); j++) {
            for (int i = 0; i < game.getWidth(); i++) {
                g.setColor(colors[game.board[i][j]]); // Set color based on game board value

                // You might want to adjust scaling factor (here: pixelSize) depending on your preferred visualisation size
                int pixelSize = 20;
                g.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize); // Draw a square (pixel)
            }
        }
    }
}
