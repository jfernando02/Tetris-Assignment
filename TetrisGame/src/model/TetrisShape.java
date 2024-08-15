// Defines the shape, movement and rotation of the Tetris blocks
package model;

public class TetrisShape {

    private final int[][] coordinates; //coordinates are [[x0,x1,x2,x3],[y0,y1,y2,y3]]
    private final int id;

    public TetrisShape(String shape){
        try {
            coordinates = TetrisShapes.valueOf(shape).getCoordinates();
            id = TetrisShapes.valueOf(shape).ordinal();
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid shape: " + shape);
        }
    }

    public int[][] rotate() {
        int[] x = new int[4];
        int[] y = new int[4];
        //Pivot is always third element
        //which co-ordinate a shape rotates around
        int pivot = 2;
        int pivotX = coordinates[0][pivot];
        int pivotY = coordinates[1][pivot];

        int[] currentX = coordinates[0];
        int[] currentY = coordinates[1];
        //Pivot becomes (0,0)
        for (int i = 0; i < coordinates[0].length; i++) {
            //[x,y] -> [y,-x]
            currentX[i] -= pivotX;
            currentY[i] -= pivotY;
        }
        //Rotate shape, pivot doesn't change as (0,0)
        for (int i = 0; i < coordinates[0].length; i++) {
            //[x,y] -> [y,-x]
            x[i] = currentY[i];
            y[i] = currentX[i]*-1;
        }
        //Add back pivot diff
        for (int i = 0; i < coordinates[0].length; i++) {
            //[x,y] -> [y,-x]
            x[i] += pivotX;
            y[i] += pivotY;
        }
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
