package model;

import java.util.Arrays;

public class TetrisAIBlock {
    private TetrisShape shape;
    private int[][] currentCoordinates;

    public TetrisAIBlock(TetrisShape shape, int currentRotation) {
        this.shape = shape;
        this.currentCoordinates = this.shape.getCoordinates(currentRotation);
        adjustCoordinates(this.currentCoordinates);
    }

    public void pivot(int rotation) {
        this.currentCoordinates = this.shape.getCoordinates(rotation);
        adjustCoordinates(this.currentCoordinates);
    }

    public int[][] getCurrentCoordinates() {
        return currentCoordinates;
    }

    //If there is no 0 in the x-coordinates some AI calculations can be hindered
    public void adjustCoordinates(int[][] coordinates) {
        boolean xContainsZero = false;
        while (!xContainsZero) {
            for (int i = 0; i < coordinates[0].length; i++) {
                if (coordinates[0][i] == 0) {
                    xContainsZero = true;
                    break;
                }
            }
            if (!xContainsZero) {
                for (int i = 0; i < coordinates[0].length; i++) {
                    coordinates[0][i] -= 1;
                }
            }
        }
    }
}
