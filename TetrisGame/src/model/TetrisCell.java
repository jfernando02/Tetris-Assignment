package model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

// Class for each individual cell in the Tetris grid
public class TetrisCell {
    int x; // X coordinate of the cell relative to the board
    int y; // Y coordinate of the cell relative to the board
    private float interpolatedY;
    String color; // Color of the cell
    Board<TetrisCell> board;
    Boolean isActive;

    public TetrisCell(int x, int y, String color, Board<TetrisCell> board) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.board = board;
        isActive = true;
        if (board != null) {
            setOnBoard();
        }
        this.interpolatedY = y;
    }

    public TetrisCell clone(){
        TetrisCell clone = new TetrisCell(x,y,color,board);
        return clone;
    }

    public void render(Graphics g, int cellSize) {
        Graphics2D g2d = (Graphics2D) g;
        float drawX = x * cellSize;
        float drawY = (float) interpolatedY * cellSize; // Use interpolatedY for smooth movement
        g2d.setColor(Color.decode(color));
        g2d.fill(new Rectangle2D.Float(drawX, drawY, cellSize, cellSize));

        // Always draw a grey grid around the cell
        g2d.setColor(Color.GRAY);
        g2d.draw(new Rectangle2D.Float(drawX, drawY, cellSize, cellSize));
    }

    //method to render the cell in the next piece field
    public void renderNextPiece(Graphics g, int cellSize, int[][] initialShape) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < initialShape[0].length; i++) {
            float drawX = initialShape[0][i] * cellSize;
            float drawY = initialShape[1][i] * cellSize;
            g2d.setColor(Color.decode(color));
            g2d.fill(new Rectangle2D.Float(drawX, drawY, cellSize, cellSize));

            // Always draw a grey grid around the cell
            g2d.setColor(Color.GRAY);
            g2d.draw(new Rectangle2D.Float(drawX, drawY, cellSize, cellSize));
        }
    }

    // Set the cell on the board
    public void setOnBoard() {
        this.board.setCell(this.x, this.y, this);
    }

    public void setBoard(Board<TetrisCell> board) {
        this.board = board;
    }

    public int getX() { return x; }

    // Set for x movement
    public void setX(int newX) { this.x = newX; }

    public int getY() { return y; }

    // Set for y movement
    public void setY(int newY) { this.y = newY; }

    // Cell destroy (sets cell to null)
    public void destroy() {
        this.board.destroyCell(this.x, this.y);
    }

    // Method to smoothly update interpolatedY towards y
    public void updateInterpolatedY() {
        // Linear interpolation formula
        this.interpolatedY += (y - interpolatedY) * 0.1f;
    }

    // Method to reset interpolatedY to the current y position
    public void resetInterpolation() {
        this.interpolatedY = this.y;
    }

}