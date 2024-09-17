package model;

import java.util.Arrays;

public class TetrisAIBlock {
    private TetrisShape shape;
    private int[][] currentCoordinates;
    private int currentRotation;

    public TetrisAIBlock(TetrisShape shape, int[][] currentCoordinates, int currentRotation) {
        this.shape = shape;
        this.currentCoordinates = currentCoordinates;
        this.currentRotation = currentRotation;
    }

    //Needs to be identical to pivot method of TetrisBlock
    public void pivot(int rotation) {
        while(rotation!=currentRotation) {
            int pivotX = this.currentCoordinates[0][1];
            int pivotY = this.currentCoordinates[1][1];
            int[][] nextRotation = shape.getCoordinates(rotation);

            for (int i = 0; i < currentCoordinates.length; i++) {
                this.currentCoordinates[0][i] = nextRotation[0][i] + pivotX - 1;
                this.currentCoordinates[1][i] = nextRotation[1][i] + pivotY;
            }
            if (this.currentRotation == 3) {
                this.currentRotation = 0;
            } else {
                this.currentRotation++;
            }
        }
    }

    public int[][] getCurrentCoordinates() {
        return currentCoordinates;
    }
}
