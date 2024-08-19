// FieldPane renders the game field where the Tetris blocks are displayed. It uses the Board class to get the current state of the game field and renders it accordingly. The cellSize parameter determines the size
// of each cell in the game field.
package ui.field;

import model.Board;
import model.TetrisCell;

import javax.swing.*;
import java.awt.*;


public class FieldPane extends JPanel {
    private Board<TetrisCell> board;
    private int cellSize;

    public FieldPane(Board<TetrisCell> board, int cellSize) {
        this.board = board;
        this.cellSize = cellSize;
        setPreferredSize(new Dimension(board.getWidth() * cellSize, board.getHeight() * cellSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) {
            return;
        }
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GRAY);
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                TetrisCell cell = board.getCell(x, y);
                if (cell != null) {
                    cell.render(g, cellSize);
                }
            }
        }
    }

    //update the board with a new board state
    public void updateBoard(Board<TetrisCell> board) {
        this.board = board;
        repaint();
    }
}