// Manages the game state, updates and overall control
package model;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
    int[][] board=new int[20][11]; //Top left is (0,0), bottom right is (20,11)
    static int[] spawn_location={3,5}; //Top middle of board
    TetrisShape active_shape;

    public Game(){
        spawn();
        update();
        display();
    }

    private void display(){
        for (int[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }
    public void play(){
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter input: ");
        String input = scanner.nextLine();  // Read user input
        clearActiveShape();
        // Process user input
        if(input.equals("up")){
            active_shape.rotate();
        }
        else if(input.equals("left") || input.equals("right")){
            active_shape.translate(input);
        }
        active_shape.translate("down");
        update();
        display();
    }

    private void spawn(){
        active_shape=new TetrisShape("LINE");
    }

    private void clearActiveShape() {
        for (int i = 0; i < active_shape.getCoordinates().length; i++) {
            int boardX = spawn_location[0] + active_shape.getCoordinates()[i][0];
            int boardY = spawn_location[1] + active_shape.getCoordinates()[i][1];
            board[boardX][boardY] = 0;
        }
    }

    private void update(){
        for (int i = 0; i < active_shape.getCoordinates().length; i++) {
            int boardX = spawn_location[0] + active_shape.getCoordinates()[i][0];
            int boardY = spawn_location[1] + active_shape.getCoordinates()[i][1];
            board[boardX][boardY]=active_shape.getId();
        }
    }
}
