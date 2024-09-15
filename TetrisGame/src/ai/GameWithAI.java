package ai;

import model.Board;
import model.games.Game;
import controller.MainFrame;
import view.panel.GamePanel;

import java.awt.event.KeyEvent;

public class GameWithAI extends Game {
    private TetrisAI ai = new TetrisAI();

    public GameWithAI(MainFrame mainFrame, GamePanel gamePanel) {
        super(mainFrame, gamePanel);
    }

    public void dropPiece() {
// Let the AI decide the best move
        Move bestMove = ai.findBestMove(this, activeShape);
// Rotate the piece
        for (int i = 0; i < bestMove.rotation; i++) {
            update(KeyEvent.VK_UP);
        }
// Move the piece to the best column
        while (activeShape.getColumn() < bestMove.col) {
            update(KeyEvent.VK_RIGHT);
        }
        while (activeShape.getColumn() > bestMove.col) {
            update(KeyEvent.VK_LEFT);
        }
// Drop the piece
        super.update(KeyEvent.VK_DOWN);
    }
}
