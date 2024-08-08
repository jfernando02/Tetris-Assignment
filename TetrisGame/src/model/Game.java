// Manages the game state, updates and overall control
package model;

import java.util.Scanner;

public class Game {
    int width=11;
    int height=20;
    int[][] board=new int[width][height]; //Top left is (0,0), bottom right is (20,11)
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
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter input: ");
        String input = scanner.nextLine();  // Read user input
        clearActiveShape();
        // Process user input
        if(input.equals("up")){
            move=active_shape.rotate();
            if(validMove(move)){
                active_shape.moveShape(move);
            }
        }
        else if(input.equals("left") || input.equals("right")){
            move=active_shape.translate(input);
            if(validMove(move)){
                active_shape.moveShape(move);
            }
        }
        move=active_shape.translate("down");
        if(validMove(move)){
            active_shape.moveShape(move);
        }
        else{
            System.out.println("Landed!");
        }
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

    private boolean validMove(int[][] coordinates){
        int x;
        int y;
        for (int[] coordinate : coordinates) {
            x = coordinate[0];
            y = coordinate[1];
            System.out.println(spawn_location[0] + x);
            System.out.println(spawn_location[1] + y);
            if (spawn_location[0] + x > width || spawn_location[0] + x < 0 ||
                    spawn_location[1] + y > height || spawn_location[1] + y < 0 ||
                    board[spawn_location[0] + x][spawn_location[1] + y] != 0) {
                return false;
            }
        }
        return true;
    }
}
