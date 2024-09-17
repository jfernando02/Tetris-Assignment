package ai;
import model.*;
import model.games.Game;

import java.util.Arrays;

public class TetrisAI {
    private Game game;
    private BoardEvaluator evaluator = new BoardEvaluator();

    public TetrisAI(Game game){
        this.game = game;
    }

    public BoardEvaluator getEvaluator() {
        return evaluator;
    }

    public Move findBestMove(TetrisBlock piece) {
        Move bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int rotation = 0; rotation < 4; rotation++) {
            TetrisAIBlock simulatedPiece = piece.convertBlock();
            simulatedPiece.pivot(rotation);
            for (int col = 0; col < game.getBoard().getWidth(); col++) {
                int[][] simulatedBoard = simulateDrop(game.getBoard().convertBoard(),
                        simulatedPiece, col);
                if(simulatedBoard!=null) {
                    double score = evaluator.evaluateBoard(simulatedBoard);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(col, rotation);
                    }
                }
            }
        }
        return bestMove;
    }

    private int[][] simulateDrop(int[][] board, TetrisAIBlock piece, int col)
    {
// Simulate dropping the piece in the given column and return the new board
        int[][] simulatedBoard = copyBoard(board);
        int dropRow = getDropRow(simulatedBoard, piece, col);
        if (dropRow == -1) {
            return null;
        }
        placePiece(simulatedBoard, piece, col, dropRow);
        return simulatedBoard;
    }
    private int getDropRow(int[][] board, TetrisAIBlock piece, int col) {
// Find the row where the piece would land if dropped in the given column
        int row = 0;
        while (canPlacePiece(board, piece, col, row)) {
            row++;
        }
        return row - 1; // Return the last valid row
    }
    private boolean canPlacePiece(int[][] board, TetrisAIBlock piece, int col,
                                  int row) {
// Check if the piece can be placed at the given column and row
        int[][] coordinates = piece.getCurrentCoordinates();
        for (int i = 0; i<coordinates[0].length; i++) {
            int x = row + coordinates[1][i];
            int y = col + coordinates[0][i];
            if (x < 0 || x >= board[0].length || y < 0 || y >=
                    board.length || board[y][x] != 0) {
                return false;
            }
        }
        return true;
    }
    private void placePiece(int[][] board, TetrisAIBlock piece, int col, int
            row) {
        // Place the piece on the board at the given position
        int[][] coordinates = piece.getCurrentCoordinates();
        for (int i = 0; i<coordinates[0].length; i++) {
            board[col + coordinates[0][i]][row + coordinates[1][i]] = 1; // 1 represents the piece
        }
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            System.arraycopy(board[y], 0, newBoard[y], 0,
                    board[0].length);
        }
        return newBoard;
    }
}
