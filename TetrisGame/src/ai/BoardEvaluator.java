package ai;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static ai.GeneticAlgorithm.*;

public class BoardEvaluator {
    private int finalHighScore;
    Map<String, Double> constants;
    private String filename = "src/ai/DNA.json";
    private Double id;

    public BoardEvaluator() {
        //Get Unfit DNA
        if(countWithFitness(filename)>=50) {
            selectAndReproduce(filename);
        }
        constants = getFirstUnfitDNA(filename);
        id = constants.get("id");
    }

    public double evaluateBoard(int[][] board) {
        Double heightFactor = constants.get("height");
        Double relativeHeightFactor = constants.get("relativeHeight");
        Double maxHeightFactor = constants.get("maxHeight");
        Double linesClearedFactor = constants.get("linesCleared");
        Double holesFactor = constants.get("holes");
        Double bumpinessFactor = constants.get("bumpiness");
        Double totalHeightScore = (double) getTotalHeight(board);
        Double relativeHeightScore = (double) getRelativeHeight(board);
        Double maxHeightScore = (double) getMaxHeight(board);
        Double holesScore = (double) getHoles(board);
        Double linesCleared = (double) getClearedLines(board);
        Double bumpinessScore = (double) getBumpiness(board);
        return (linesClearedFactor * linesCleared) + (heightFactor * totalHeightScore) + (relativeHeightFactor * relativeHeightScore) +
        (maxHeightFactor * maxHeightScore) + (holesFactor * holesScore) + (bumpinessFactor * bumpinessScore);
    }
    private int getTotalHeight(int[][] board) {
        // Calculate total height of all columns, and square it
        int totalHeight = 0;
        for (int x = 0; x < board[0].length; x++) {
            int columnHeight = 0;
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != 0) {
                    columnHeight = board.length - y;
                    break;
                }
            }
            totalHeight += columnHeight;
        }
        return totalHeight;
    }

    private int getMaxHeight(int[][] board) {
        int maxHeight = 0;
        for (int x = 0; x < board[0].length; x++) {
            int columnHeight = 0;
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != 0) {
                    columnHeight = board.length - y;
                    break;
                }
            }
            maxHeight = Math.max(maxHeight, columnHeight);
        }
        return maxHeight;
    }

    private int getRelativeHeight(int[][] board) {
        int maxHeight = 0;
        int minHeight = board.length;
        for (int x = 0; x < board[0].length; x++) {
            int columnHeight = 0;
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != 0) {
                    columnHeight = board.length - y;
                    break;
                }
            }
            minHeight = Math.min(minHeight, columnHeight);
            maxHeight = Math.max(maxHeight, columnHeight);
        }
        return maxHeight-minHeight;
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

    public void setFitness(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Read data from file
            List<Map<String, Double>> dnas = mapper.readValue(new File(filename),
                    new TypeReference<>() {
                    });
            // Find and update the DNA with matching id
            for (Map<String, Double> dna : dnas) {
                if (dna.get("id").equals(id)) {
                    dna.put("fitness", (double) finalHighScore);
                    break;
                }
            }
            // Write the updated data back to the file
            System.out.println("Fitness set");
            mapper.writeValue(new File(filename), dnas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
