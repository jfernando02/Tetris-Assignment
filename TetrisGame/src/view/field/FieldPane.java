// FieldPane renders the game field where the Tetris blocks are displayed. It uses the Board class to get the current state of the game field and renders it accordingly. The cellSize parameter determines the size
// of each cell in the game field.
// FieldPane.java
package view.field;

import model.Board;
import model.TetrisCell;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;

// This Class is responsible for rendering the game field (where the tetris blocks are displayed)
// For extended mode, two threads will each render a FieldPane unto a PlayPanel both nested in a GamePanel
public class FieldPane extends JPanel {
    private Board<TetrisCell> board;
    private int cellSize;

    public FieldPane(Board<TetrisCell> board, int cellSize) {
        this.board = board;
        this.cellSize = cellSize;
        setPreferredSize(new Dimension(board.getWidth() * cellSize + 2, board.getHeight() * cellSize + 2));
        Border border = BorderFactory.createLineBorder(Color.RED, 2);
        setBorder(border);
    }

    // This method is called by the system to render the game field
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.GRAY);
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                g2d.draw(new Rectangle2D.Float(x * cellSize, y * cellSize, cellSize, cellSize));
            }
        }

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                TetrisCell cell = board.getCell(x, y);
                if (cell != null) {
                    cell.updateInterpolatedY();
                    cell.render(g, cellSize);
                }
            }
        }
    }

    // To refresh the game field with the updated state
    public void updateBoard(Board<TetrisCell> board) {
        this.board = board;
        repaint();
    }
}