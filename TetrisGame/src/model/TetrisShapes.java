package model;

import java.util.Arrays;
import java.util.Random;

public enum TetrisShapes {
    NO_SHAPE(new int[][] {{}}),
    LINE(new int[][] {{-2, -1, 0, 1}, {0, 0, 0, 0}}),
    SQUARE(new int[][] {{0, 1, 0, 1}, {0, 0, 1, 1}}),
    T_SHAPE(new int[][] {{-1, 0, 1, 0}, {0, 0, 0, -1}}),
    S_SHAPE(new int[][] {{0, 0, 1, 1}, {1, 0, 0, -1}}),
    Z_SHAPE(new int[][] {{0, 0, -1, -1}, {1, 0, 0, -1}}),
    L_SHAPE(new int[][] {{-1, 0, 0, 0}, {1, 1, 0, -1}}),
    REVERSE_L_SHAPE(new int[][] {{1, 0, 0, 0}, {1, 1, 0, -1}});

    private final int[][] coordinates;

    TetrisShapes(int[][] coordinates) {
        this.coordinates = coordinates;
    }

    public int[][] getCoordinates() {
        int[][] copy = new int[coordinates.length][];
        for (int row = 0; row < coordinates.length; row++) {
            copy[row] = Arrays.copyOf(coordinates[row], coordinates[row].length);
        }
        return copy;
    }

    public static TetrisShapes getRandomShape() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
