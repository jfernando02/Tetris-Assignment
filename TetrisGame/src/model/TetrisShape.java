// Class that handles instantiation of Tetris shapes and their rotational behaviour
package model;

import java.util.Random;

public enum TetrisShape {
    I, // Cyan
    O, // Yellow
    T, // Purple
    S, // Green
    Z, // Red
    L, // Orange
    J; // Blue

    public static TetrisShape getRandomShape() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    //hard coded (by hand) coordinates for each shape in each rotation:
    // https://strategywiki.org/wiki/File:Tetris_rotation_Nintendo.png
    // clockwise rotation: 0 -> 1 -> 2 -> 3 -> 0
    // anti-clockwise rotation: 0 -> 3 -> 2 -> 1 -> 0
    public int[][] getCoordinates(int rotation) {
        return switch (this) {
            case I -> switch (rotation) {
                case 0 -> new int[][]{{0, 1, 2, 3}, {1, 1, 1, 1}};
                case 1 -> new int[][]{{1, 1, 1, 1}, {0, 1, 2, 3}};
                case 2 -> new int[][]{{0, 1, 2, 3}, {1, 1, 1, 1}};
                case 3 -> new int[][]{{1, 1, 1, 1}, {0, 1, 2, 3}};
                default -> new int[][]{{0, 1, 2, 3}, {2, 2, 2, 2}};
            };
            case O -> new int[][]{{0, 1, 0, 1}, {0, 0, 1, 1}};
            case T -> switch (rotation) {
                case 0 -> new int[][]{{0, 1, 1, 2}, {1, 1, 2, 1}};
                case 1 -> new int[][]{{1, 1, 1, 0}, {0, 1, 2, 1}};
                case 2 -> new int[][]{{0, 1, 1, 2}, {1, 1, 0, 1}};
                case 3 -> new int[][]{{1, 1, 1, 2}, {0, 1, 2, 1}};
                default -> new int[][]{{0, 1, 1, 2}, {1, 1, 2, 1}};
            };
            case S -> switch (rotation) {
                case 0 -> new int[][]{{0, 1, 1, 2}, {1, 1, 0, 0}};
                case 1 -> new int[][]{{1, 1, 2, 2}, {0, 1, 1, 2}};
                case 2 -> new int[][]{{0, 1, 1, 2}, {1, 1, 0, 0}};
                case 3 -> new int[][]{{1, 1, 2, 2}, {0, 1, 1, 2}};
                default -> new int[][]{{0, 1, 1, 2}, {1, 1, 0, 0}};
            };
            case Z -> switch (rotation) {
                case 0 -> new int[][]{{0, 1, 1, 2}, {0, 0, 1, 1}};
                case 1 -> new int[][]{{1, 1, 0, 0}, {0, 1, 1, 2}};
                case 2 -> new int[][]{{0, 1, 1, 2}, {0, 0, 1, 1}};
                case 3 -> new int[][]{{1, 1, 0, 0}, {0, 1, 1, 2}};
                default -> new int[][]{{0, 1, 1, 2}, {0, 0, 1, 1}};
            };
            case L -> switch (rotation) {
                case 0 -> new int[][]{{1, 1, 2, 3}, {0, 1, 0, 0}};
                case 1 -> new int[][]{{0, 1, 1, 1}, {0, 0, 1, 2}};
                case 2 -> new int[][]{{0, 1, 2, 2}, {1, 1, 1, 0}};
                case 3 -> new int[][]{{1, 1, 1, 0}, {0, 1, 2, 2}};
                default -> new int[][]{{0, 0, 1, 2}, {1, 2, 1, 1}};
            };
            case J -> switch (rotation) {
                case 0 -> new int[][]{{1, 2, 3, 3}, {0, 0, 0, 1}};
                case 1 -> new int[][]{{1, 1, 1, 0}, {0, 1, 2, 2}};
                case 2 -> new int[][]{{0, 0, 1, 2}, {0, 1, 1, 1}};
                case 3 -> new int[][]{{2, 1, 1, 1}, {0, 0, 1, 2}};
                default -> new int[][]{{0, 1, 2, 2}, {1, 1, 1, 2}};
            };
            default -> new int[][]{{}};
        };
    }


    // for colour rendering on the FieldPane grid (game.board)
    public String getColor() {
        return switch (this) {
            case I -> "#00FFFF"; // cyan
            case O -> "#FFFF00"; // yellow
            case T -> "#800080"; // purple
            case S -> "#00FF00"; // green
            case Z -> "#FF0000"; // red
            case L -> "#FFA500"; // orange
            case J -> "#0000FF"; // blue
            default -> "#000000"; // black
        };
    }
}