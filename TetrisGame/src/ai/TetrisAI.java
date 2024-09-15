package ai;
import model.Point;
import model.TetrisBlock;
import model.games.Game;

public class TetrisAI {
    private BoardEvaluator evaluator = new BoardEvaluator();

    public Move findBestMove(Game game, TetrisBlock piece) {
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        for (int rotation = 0; rotation < 4; rotation++) {
            for (int col = 0; col < game.getBoard().getWidth(); col++) {
                TetrisBlock simulatedPiece = piece.clone();
                simulatedPiece.pivot(rotation);
                int[][] simulatedBoard = simulateDrop(game.getBoard().convertBoard(),
                        simulatedPiece, col);
                int score = evaluator.evaluateBoard(simulatedBoard);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new Move(col, rotation);
                }
            }
        }
        return bestMove;
    }
    private int[][] simulateDrop(int[][] board, TetrisBlock piece, int col)
    {
// Simulate dropping the piece in the given column and return the new board
        int[][] simulatedBoard = copyBoard(board);
        int dropRow = getDropRow(simulatedBoard, piece, col);
        placePiece(simulatedBoard, piece, col, dropRow);
        return simulatedBoard;
    }
    private int getDropRow(int[][] board, TetrisBlock piece, int col) {
// Find the row where the piece would land if dropped in the given column
        int row = 0;
        while (canPlacePiece(board, piece, col, row)) {
            row++;
        }
        return row - 1; // Return the last valid row
    }
    private boolean canPlacePiece(int[][] board, TetrisBlock piece, int col,
                                  int row) {
// Check if the piece can be placed at the given column and row
        for (Point p : piece.getCellCoordinates()) {
            int x = col + p.getX();
            int y = row + p.getY();
            if (x < 0 || x >= board[0].length || y < 0 || y >=
                    board.length || board[y][x] != 0) {
                return false;
            }
        }
        return true;
    }
    private void placePiece(int[][] board, TetrisBlock piece, int col, int
            row) {
// Place the piece on the board at the given position
        for (Point p : piece.getCellCoordinates()) {
            board[row + p.getY()][col + p.getX()] = 1; // 1 represents the piece
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
