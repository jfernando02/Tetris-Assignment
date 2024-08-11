// Manages the game state, updates and overall control
package model;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    int width=11;
    int height=20;
    int[][] board=new int[width][height]; //Top left is (0,0), bottom right is (10,19)
    static int[] spawn_location={5,3}; //Top middle of board
    TetrisShape active_shape;

    public Game(){
        spawn();
        update();
        display();
    }

    private void display(){
        for(int j = 0; j < height; j++) {
            for(int i = 0; i < width; i++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void play(){
        int[][] move;
        int[][] gravity;
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter input: ");
        String input = scanner.nextLine();  // Read user input
        clearActiveShape();
        // Process user input
        if(input.equals("up")){
            move=active_shape.rotate();
        }
        else if(input.equals("left") || input.equals("right")){
            move=active_shape.translate(input);
        }
        else{
            move=active_shape.getCoordinates();
        }
        if(validMove(move)){
            active_shape.moveShape(move);
        }
        gravity=active_shape.translate("down");
        if(validMove(gravity)){
            active_shape.moveShape(gravity);
        }
        else{
            update();
            clearLines();
            spawn();
        }
        update();
        display();
    }

    private void spawn(){
        active_shape= new TetrisShape(TetrisShapes.getRandomShape().name());
    }

    private void clearActiveShape() {
        for (int i = 0; i < active_shape.getCoordinates()[0].length; i++) {
            int boardX = spawn_location[0] + active_shape.getCoordinates()[0][i];
            int boardY = spawn_location[1] + active_shape.getCoordinates()[1][i];
            board[boardX][boardY] = 0;
        }
    }

    private void clearLines() {
        ArrayList<Integer> fullLines = new ArrayList<>();

        // First, find all full lines
        for (int j = 0; j < height; j++) {
            boolean isLineFull = true;
            for (int i = 0; i < width; i++) {
                if (board[i][j] == 0) {
                    isLineFull = false;
                    break;
                }
            }
            if (isLineFull) {
                fullLines.add(j);
            }
        }

        // Now, remove the full lines and shift down the rest
        for (int fullLineIndex = fullLines.size() - 1; fullLineIndex >= 0; fullLineIndex--) {
            int fullLine = fullLines.get(fullLineIndex);

            for (int j = fullLine; j > 0; j--) {
                for (int i = 0; i < width; i++) {
                    board[i][j] = board[i][j - 1];
                }
            }

            for (int i = 0; i < width; i++) {
                board[i][0] = 0;
            }
        }
    }

    private void update(){
        for (int i = 0; i < active_shape.getCoordinates()[0].length; i++) {
            int boardX = spawn_location[0] + active_shape.getCoordinates()[0][i];
            int boardY = spawn_location[1] + active_shape.getCoordinates()[1][i];
            board[boardX][boardY]=active_shape.getId();
        }
    }

    private boolean validMove(int[][] coordinates){
        int x;
        int y;
        for (int i = 0; i < coordinates[0].length; i++) {
            x = coordinates[0][i];
            y = coordinates[1][i];
            if (spawn_location[0] + x >= width || spawn_location[0] + x < 0 ||
                    spawn_location[1] + y >= height || spawn_location[1] + y < 0){
                return false;
            }
            else if(board[spawn_location[0] + x][spawn_location[1] + y] != 0) {
                return false;
            }

        }
        return true;
    }
}
