package model;

import java.awt.*;

// Class for each individual cell in the Tetris grid
public class TetrisCell {
    int idShape; // ID of the Tetris piece to which the cell belongs
    int x; // X coordinate of the cell relative to the board
    int y; // Y coordinate of the cell relative to the board
    String color; // Color of the cell
    Board<TetrisCell> board;
    Boolean isActive;
    public TetrisCell(int x, int y, String color, Board<TetrisCell> board) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.board = board;
        isActive = true;
        setOnBoard();
    }

    // Render the cell on the game board (FieldPane)
    public void render(Graphics g, int cellSize) {
        int drawX = x * cellSize;
        int drawY = y * cellSize;
        g.setColor(Color.decode(color));
        g.fillRect(drawX, drawY, cellSize, cellSize);

        // Always draw a grey grid around the cell
        g.setColor(Color.GRAY);
        g.drawRect(drawX, drawY, cellSize, cellSize);
    }

    // Set the cell on the board
    public void setOnBoard() {
        this.board.setCell(this.x, this.y, this);
    }

    public int getX() { return x; }

    // Set for x movement
    public void setX(int newX) { this.x = newX; }

    public int getY() {
        return y;
    }

    // Set for y movement
    public void setY(int newY) { this.y = newY; }

    //cell destroy (sets cell to null)
    public void destroy() { this.board.destroyCell(this.x, this.y); }
}
