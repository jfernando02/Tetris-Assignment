package ui.field;
import model.Board;
import model.TetrisBlock;
import model.TetrisCell;
import model.TetrisShape;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;


// FieldPane for the next piece field
public class NextPieceFieldPane extends JPanel {
    int cellSize;
    TetrisBlock nextPiece;
    //get the colour
    ArrayList<TetrisCell> cells;

    public NextPieceFieldPane(TetrisBlock nextPiece, int cellSize) {
        this.nextPiece = nextPiece;
        this.cellSize = cellSize;
        // get colour of just one cell
        this.cells = nextPiece.getCells();
        //make a board with 4x4 cells
        setPreferredSize(new Dimension(4 * cellSize, 4 * cellSize ));
        //set the size as absolute of the setPreferredSize
        setMinimumSize(new Dimension(4 * cellSize, 4 * cellSize));
        setMaximumSize(new Dimension(4 * cellSize, 4 * cellSize));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }
    // render the field for the next piece
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (nextPiece == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.GRAY);

        //draw the grid
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                g2d.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        // renderField the next piece
        for (TetrisCell cell : cells) {
            cell.renderNextPiece(g, cellSize, nextPiece.getShape());
        }
    }

    // render the next piece
    public void renderNextPiece(TetrisBlock nextPiece) {
        this.nextPiece = nextPiece;
        this.cells = nextPiece.getCells();
        repaint();
    }
}