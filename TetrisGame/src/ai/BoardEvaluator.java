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
        if(countWithFitness(filename)>=52) {
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
        int totalHeight = 0;
        for (int col =0; col < board.length; col++) {
            totalHeight += getColumnHeight(board, col);
        }
        return totalHeight;
    }

    private int getMaxHeight(int[][] board) {
        int maxHeight = 0;
        for (int col =0; col < board.length; col++) {
            maxHeight = Math.max(maxHeight, getColumnHeight(board, col));
        }
        return maxHeight;
    }

    private int getRelativeHeight(int[][] board) {
        int maxHeight = 0;
        int minHeight = Integer.MAX_VALUE;
        for (int col =0; col < board.length; col++) {
            minHeight = Math.min(minHeight, getColumnHeight(board, col));
            maxHeight = Math.max(maxHeight, getColumnHeight(board, col));
        }
        return maxHeight-minHeight;
    }

    private int getHoles(int[][] board) {
        // Calculate the number of holes (empty spaces beneath filled blocks)
        int holes = 0;
        for (int col = 0; col < board.length; col++) {
            boolean foundBlock = false;
            for (int row = 0; row < board[col].length; row++) {
                if (board[col][row] != 0) {
                    foundBlock = true;
                } else if (foundBlock && board[col][row] == 0) {
                    holes++;
                }
            }
        }
        return holes;
    }

    private int getClearedLines(int[][] board) {
        // Calculate how many full lines are cleared
        int clearedLines = 0;
        for (int row = 0; row < board[0].length; row++) {
            boolean isLineFull = true;
            for (int col = 0; col < board.length; col++) {
                if (board[col][row] == 0) {
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
        for (int col = 0; col < board.length - 1; col++) {
            int colHeight1 = getColumnHeight(board, col);
            int colHeight2 = getColumnHeight(board, col + 1);
            bumpiness += Math.abs(colHeight1 - colHeight2);
        }
        return bumpiness;
    }

    private int getColumnHeight(int[][] board, int col) {
        for (int row = 0 ; row < board[col].length; row++){
            if (board[col][row] != 0) {
                return board[col].length-row;
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
