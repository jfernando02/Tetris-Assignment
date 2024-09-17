package model;

public class Move {
    public int col;
    public int rotation;
    public int[][] board;
    public Move(int col, int rotation) {
        this.col=col;
        this.rotation=rotation;
    }

    public void setSimulatedBoard(int[][] board) {
        this.board=board;
    }

    public int[][] getSimulatedBoard() {
        return board;
    }
}
