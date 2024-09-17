package server.clientSide;

import java.util.Arrays;

public class tetrisGameInfo {
    // Fields
    private int width;
    private int height;
    private int[][] cells;
    private int[][] currentShape;
    private int[][] nextShape;

    // Constructor
    public tetrisGameInfo(int width, int height, int[][] cells, int[][] currentShape, int[][] nextShape) {
        this.width = width;
        this.height = height;
        this.cells = cells;
        this.currentShape = currentShape;
        this.nextShape = nextShape;
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getCells() {
        return cells;
    }

    public int[][] getCurrentShape() {
        return currentShape;
    }

    public int[][] getNextShape() {
        return nextShape;
    }

    // Setters
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public void setCurrentShape(int[][] currentShape) {
        this.currentShape = currentShape;
    }

    public void setNextShape(int[][] nextShape) {
        this.nextShape = nextShape;
    }

    // toString Method
    @Override
    public String toString() {
        return "tetrisGameInfo{" +
                "width=" + width +
                ",height=" + height +
                ",cells=" + Arrays.deepToString(cells) +
                ",currentShape=" + Arrays.deepToString(currentShape) +
                ",nextShape=" + Arrays.deepToString(nextShape) +
                '}';
    }
}

