// Defines the shape, movement and rotation of the Tetris blocks
package model;

public class TetrisShape {
    public static final int[][] NO_SHAPE = {{}};
    public static final int[][] LINE = {{-2,0},{-1,0},{0,0},{1,0}};
    public static final int[][] SQUARE = {{0,0},{1,0},{0,1},{1,1}};
    public static final int[][] T_SHAPE = {{-1,0},{0,0},{1,0},{0,-1}};
    public static final int[][] S_SHAPE = {{0,1},{0,0},{1,0},{1,-1}};
    public static final int[][] Z_SHAPE = {{0,1},{0,0},{-1,0},{-1,-1}};
    public static final int[][] L_SHAPE = {{-1,1},{0,1},{0,0},{0,-1}};
    public static final int[][] REVERSE_L_SHAPE = {{1,1},{0,1},{0,0},{0,-1}};

    private int[][] coordinates; //coordinates are [y,x]
    private int id;

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

    public void rotate() {
        for (int i = 0; i < coordinates.length; i++) {
            int original_x=coordinates[i][0];
            int original_y=coordinates[i][1];
            coordinates[i][0]=original_y*-1;
            coordinates[i][1]=original_x*-1;
        }
    }

    public void translate(String direction){
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
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i][0]+=y_change;
            coordinates[i][1]+=x_change;
        }
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    public int getId() {
        return id;
    }
}
