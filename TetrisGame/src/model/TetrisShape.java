// Defines the shape, movement and rotation of the Tetris blocks
package model;

import java.util.Arrays;

public class TetrisShape {
    public static final int[][] NO_SHAPE = {{}};
    public static final int[][] LINE = {{-2, -1, 0, 1}, {0, 0, 0, 0}};
    public static final int[][] SQUARE = {{0, 1, 0, 1}, {0, 0, 1, 1}};
    public static final int[][] T_SHAPE = {{-1, 0, 1, 0}, {0, 0, 0, -1}};
    public static final int[][] S_SHAPE = {{0, 0, 1, 1}, {1, 0, 0, -1}};
    public static final int[][] Z_SHAPE = {{0, 0, -1, -1}, {1, 0, 0, -1}};
    public static final int[][] L_SHAPE = {{-1, 0, 0, 0}, {1, 1, 0, -1}};
    public static final int[][] REVERSE_L_SHAPE = {{1, 0, 0, 0}, {1, 1, 0, -1}};

    private int[][] coordinates; //coordinates are [[x0,x1,x2,x3],[y0,y1,y2,y3]]
    private final int id;

    public TetrisShape(String shape){
        switch (shape.toUpperCase()) {
            case "NO_SHAPE":
                coordinates = NO_SHAPE;
                id = 0;
                break;
            case "LINE":
                coordinates = LINE;
                id = 1;
                break;
            case "SQUARE":
                coordinates = SQUARE;
                id = 2;
                break;
            case "T_SHAPE":
                coordinates = T_SHAPE;
                id = 3;
                break;
            case "S_SHAPE":
                coordinates = S_SHAPE;
                id = 4;
                break;
            case "Z_SHAPE":
                coordinates = Z_SHAPE;
                id = 5;
                break;
            case "L_SHAPE":
                coordinates = L_SHAPE;
                id = 6;
                break;
            case "REVERSE_L_SHAPE":
                coordinates = REVERSE_L_SHAPE;
                id = 7;
                break;
            default:
                throw new IllegalArgumentException("Invalid shape: " + shape);
        }
    }

    //Doesn't work as intended
    public int[][] rotate() {
        int[] x = new int[4];
        int[] y = new int[4];
        System.out.println(Arrays.deepToString(coordinates));
        for (int i = 0; i < coordinates[0].length; i++) {
            //[x,y] -> [y,-x]
            x[i] = coordinates[1][i];
            y[i] = coordinates[0][i]*-1;
        }
        System.out.println(Arrays.deepToString(new int[][]{x, y}));
        return new int[][]{x,y};
    }

    public int[][] translate(String direction){
        int[] x = new int[4];
        int[] y = new int[4];
        int x_change=0;
        int y_change=0;
        switch(direction.toUpperCase()){
            case "LEFT":
                x_change=-1;
                break;
            case "RIGHT":
                x_change=1;
                break;
            case "DOWN":
                y_change=1;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        for (int i = 0; i < coordinates[0].length; i++) {
            x[i]=coordinates[0][i]+x_change;
            y[i]=coordinates[1][i]+y_change;
        }
        return new int[][]{x,y};
    }

    public void moveShape(int[][]xy){
        for (int i = 0; i < coordinates[0].length; i++) {
            coordinates[0][i]=xy[0][i];
            coordinates[1][i]=xy[1][i];
        }
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    public int getId() {
        return id;
    }
}
