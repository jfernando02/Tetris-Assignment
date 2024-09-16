package model;

import java.util.Arrays;

public class TetrisAIBlock {
    private TetrisShape shape;
    private int[][] currentCoordinates;

    public TetrisAIBlock(TetrisShape shape, int currentRotation) {
        this.shape = shape;
        this.currentCoordinates = shape.getCoordinates(currentRotation);
    }

    public void pivot(int rotation) {
        this.currentCoordinates = shape.getCoordinates(rotation);
    }

    public int[][] getCurrentCoordinates() {
        return currentCoordinates;
    }

    public int getWidth() {
        int[] xCoordinates = currentCoordinates[0];
        int min = Arrays.stream(xCoordinates).min().getAsInt();
        int max = Arrays.stream(xCoordinates).max().getAsInt();

        return max - min + 1;
    }
}
