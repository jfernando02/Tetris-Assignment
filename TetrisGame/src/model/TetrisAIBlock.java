package model;

public class TetrisAIBlock {
    private TetrisShape shape;
    private int[][] currentCoordinates;

    public TetrisAIBlock() {
        this.shape = TetrisShape.getRandomShape();
        this.currentCoordinates = shape.getCoordinates(0);
    }

    public void setShape(TetrisShape shape) {
        this.shape = shape;
    }

    public void pivot(int rotation) {
        this.currentCoordinates = shape.getCoordinates(rotation);
    }

    public int[][] getCurrentCoordinates() {
        return currentCoordinates;
    }
}
