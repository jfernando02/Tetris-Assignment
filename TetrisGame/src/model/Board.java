package model;
import controller.MainFrame;

public class Board<T> {
    private T[][] board;
    private int width;
    private int height;
    private int spawnX;
    private int spawnY;
    private MainFrame mainFrame;
    Game game;
    int linesCleared = 0;


    public Board(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.width = mainFrame.getConfigData().getFieldWidth();
        this.height = mainFrame.getConfigData().getFieldHeight();
        this.board = (T[][]) new Object[width][height];
        //get spawn from Game
        this.spawnX = (this.width /2-2);
        this.spawnY = 0;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    // for expanding width and height of the board
    public void refreshBoard() {
        this.width = mainFrame.getConfigData().getFieldWidth();
        this.height = mainFrame.getConfigData().getFieldHeight();
        this.board = (T[][]) new Object[width][height];
        this.spawnX = (this.width /2-2);
        this.spawnY = 0;
    }

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

    //board clear for reset
    public void clearBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                destroyCell(i, j);
            }
        }
    }

    public MainFrame getMainFrame() {
        if (mainFrame != null) {
            return mainFrame;
        }
        return null;
    }

}
