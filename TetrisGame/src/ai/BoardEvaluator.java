package ai;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static ai.GeneticAlgorithm.getDNA;
import static ai.GeneticAlgorithm.mutate;

public class BoardEvaluator {
    private int finalHighScore;
    private int currentHighScore;
    int[] constants;

    public int evaluateBoard(int[][] board) {
        //Get DNA and add a random mutation
        constants = getDNA("src/ai/DNA.txt");
        mutate(constants);
        currentHighScore = constants[0];
        int heightFactor = constants[1];
        int linesClearedFactor = constants[2];
        int holesFactor = constants[3];
        int bumpinessFactor = constants[4];
        int heightScore = getHeight(board);
        int holesScore = getHoles(board);
        int linesCleared = getClearedLines(board);
        int bumpinessScore = getBumpiness(board);
        return (linesClearedFactor * linesCleared) - (heightFactor * heightScore) - (holesFactor * holesScore)
                - (bumpinessFactor * bumpinessScore);
    }
    private int getHeight(int[][] board) {
// Calculate the height of the pile (the highest filled row)
        int height = 0;
        for (int x = 0; x < board[0].length; x++) {
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != 0) {
                    height = Math.max(height, board.length - y);
                    break;
                }
            }
        }
        return height;
    }
    private int getHoles(int[][] board) {
// Calculate the number of holes (empty spaces beneath filled blocks)
        int holes = 0;
        for (int x = 0; x < board[0].length; x++) {
            boolean foundBlock = false;
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != 0) {
                    foundBlock = true;
                } else if (foundBlock && board[y][x] == 0) {
                    holes++;
                }
            }
        }
        return holes;
    }
    private int getClearedLines(int[][] board) {
// Calculate how many full lines are cleared
        int clearedLines = 0;
        for (int y = 0; y < board.length; y++) {
            boolean isLineFull = true;
            for (int x = 0; x < board[0].length; x++) {
                if (board[y][x] == 0) {
                    isLineFull = false;
                    break;
                }
            }
            if (isLineFull) {
                clearedLines++;
            }
        }
        return clearedLines;
    }
    private int getBumpiness(int[][] board) {
// Calculate the bumpiness of the surface
        int bumpiness = 0;
        for (int x = 0; x < board[0].length - 1; x++) {
            int colHeight1 = getColumnHeight(board, x);
            int colHeight2 = getColumnHeight(board, x + 1);
            bumpiness += Math.abs(colHeight1 - colHeight2);
        }
        return bumpiness;
    }
    private int getColumnHeight(int[][] board, int col) {
        for (int y = 0; y < board.length; y++) {
            if (board[y][col] != 0) {
                return board.length - y;
            }
        }
        return 0;
    }

    public void setFinalHighScore(int finalHighScore){
        this.finalHighScore = finalHighScore;
    }

    public void naturalSelection(String filename){
        if(this.finalHighScore>this.currentHighScore){
            constants[0] = finalHighScore;
            try {
                PrintWriter writer = new PrintWriter(filename, "UTF-8");
                for(int value : constants) {
                    writer.println(value);
                }
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
