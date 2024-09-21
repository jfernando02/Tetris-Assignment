package model;
import controller.MainFrame;
import model.games.Game;

import java.util.ArrayList;
import java.util.Arrays;

public class Board<T> {
    private T[][] board;
    private int width;
    private int height;
    private int spawnX;
    private int spawnY;
    private MainFrame mainFrame;
    Game game;

    public Board(MainFrame mainFrame, Game game) {
        this.mainFrame = mainFrame;
        this.game = game;
        this.width = mainFrame.getConfigData().getFieldWidth();
        this.height = mainFrame.getConfigData().getFieldHeight();
        this.board = (T[][]) new Object[width][height];
        //get spawn from Game
        this.spawnX = (this.width / 2 - 2);
        this.spawnY = 0;
    }

    /*
    // for expanding width and height of the board
    public void refreshBoard() {
        this.width = mainFrame.getConfigData().getFieldWidth();
        this.height = mainFrame.getConfigData().getFieldHeight();
        this.board = (T[][]) new Object[width][height];
        this.spawnX = (this.width / 2 - 2);
        this.spawnY = 0;
    }
    */


    public void setCell(int x, int y, T cell) {
        board[x][y] = cell;
    }

    public T getCell(int x, int y) {
        return board[x][y];
    }

    public void destroyCell(int x, int y) {
        board[x][y] = null;
    }

    public boolean isOccupied(int x, int y) {
        return board[x][y] != null;
    }

    //logic for line clearing
    public void dropCellTo(int x, int y, TetrisCell cell) {
        destroyCell(cell.getX(), cell.getY());
        cell.setX(x);
        cell.setY(y);
        setCell(x, y, (T) cell);
    }

    //Clears the line and shifts the cells above down
    public void clearLines(int y) {
        for (int j = y; j > 0; j--) {
            for (int i = 0; i < width; i++) {
                T cell = getCell(i, j - 1);
                if (cell != null) {
                    dropCellTo(i, j, (TetrisCell) cell);
                } else {
                    destroyCell(i, j);
                }
            }
        }
        //clear line sound
        try {
            mainFrame.playSound("src/resources/sounds/lineClear.wav", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Clear all complete lines (accessed by Game) and return the number of lines cleared
    public int clearCompleteLines() {
        int clearedLines = 0;
        for (int i = height - 1; i >= 0; i--) {
            boolean rowComplete = true;
            for (int j = 0; j < width; j++) {
                if (board[j][i] == null) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) {
                clearedLines++;
                clearLines(i);
                i++; // Recheck the same row after shifting
            }
        }
        return clearedLines;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public boolean isActivePieceCell(int x, int y) {
        if(game.getActiveShape()!=null) {
            for (TetrisCell cell : game.getActiveShape().getCells()) {
                if (cell.getX() == x && cell.getY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] convertBoard() {
        int[][] convertedBoard = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(board[j][i] == null || isActivePieceCell(j, i)) {
                    convertedBoard[j][i] = 0;
                } else {
                    convertedBoard[j][i] = 1;
                }
            }
        }
        return convertedBoard;
    }

    //board clear for reset
    public void clearBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                destroyCell(i, j);
            }
        }
    }
}
