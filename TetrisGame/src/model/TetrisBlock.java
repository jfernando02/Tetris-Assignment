// TetrisBlock.java
package model;

import java.awt.*;
import java.util.ArrayList;

public class TetrisBlock {
    private TetrisShape shape;
    private int currentRotation;
    private Board<TetrisCell> board;
    private boolean hasLanded;
    private ArrayList<TetrisCell> cells = new ArrayList<>();
    private long landTime; // Time when the block landed
    private static final long BUFFER_TIME = 500; // Buffer time in milliseconds

    public TetrisBlock(Board<TetrisCell> board) {
        this.board = board;
        this.hasLanded = false;
    }

    public TetrisBlock spawnBlock(int id) {
        this.shape = TetrisShape.getRandomShape();
        this.currentRotation = 0;
        int[][] cells = shape.getCoordinates(this.currentRotation);

        for (int i = 0; i < 4; i++) {
            int cellX = cells[0][i] + this.board.getSpawnX();
            int cellY = cells[1][i] + this.board.getSpawnY();
            TetrisCell cell = new TetrisCell(cellX, cellY, shape.getColor(), this.board);
            this.cells.add(cell);
        }
        return this;
    }

    public boolean leftCollision() {
        for (TetrisCell cell : this.cells) {
            if (cell.getX() == 0 || (board.getCell(cell.getX() - 1, cell.getY()) != null && !board.getCell(cell.getX() - 1, cell.getY()).isActive)) {
                return true;
            }
        }
        return false;
    }

    public boolean rightCollision() {
        for (TetrisCell cell : this.cells) {
            if (cell.getX() == board.getWidth() - 1 || (board.getCell(cell.getX() + 1, cell.getY()) != null && !board.getCell(cell.getX() + 1, cell.getY()).isActive)) {
                return true;
            }
        }
        return false;
    }

    public boolean bottomCollision() {
        for (TetrisCell cell : this.cells) {
            if (cell.getY() == board.getHeight() - 1 || (board.isOccupied(cell.getX(), cell.getY() + 1) && !this.cells.contains(board.getCell(cell.getX(), cell.getY() + 1)))) {
                return true;
            }
        }
        return false;
    }

    public void moveCells(int moveX, int moveY) {
        for (TetrisCell cell : this.cells) {
            cell.destroy();
        }
        for (TetrisCell cell : this.cells) {
            int newX = cell.getX() + moveX;
            int newY = cell.getY() + moveY;
            if (newX >= 0 && newX < board.getWidth() && newY >= 0 && newY < board.getHeight()) {
                cell.setX(newX);
                cell.setY(newY);
            }
        }
        for (TetrisCell cell : this.cells) {
            cell.setOnBoard();
        }
    }


    public void softDrop() {
        if (bottomCollision()) {
            if (!hasLanded) {
                hasLanded = true;
                landTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - landTime >= BUFFER_TIME && !hasEmptyCellsUnderneath()
                    && bottomCollision()) {
                System.out.println("Block has landed via buffered soft drop");
                deactivate();
            }
        } else {
            moveCells(0, 1);
            for (TetrisCell cell : this.cells) {
                cell.updateInterpolatedY();
            }
            hasLanded = false; // Reset landing status if moved
        }
    }

    public void hardDrop() {
        while (!bottomCollision()) {
            moveCells(0, 1);
        }
        deactivate();
    }

    public void moveLeft() {
        if (!leftCollision() && canMove(-1, 0)) {
            moveCells(-1, 0);
            for (TetrisCell cell : this.cells) {
                cell.resetInterpolation();
            }
        }
    }

    public void moveRight() {
        if (!rightCollision() && canMove(1, 0)) {
            moveCells(1, 0);
            for (TetrisCell cell : this.cells) {
                cell.resetInterpolation();
            }
        }
    }

    public void rotateLeft() {
        if (!hasLanded || System.currentTimeMillis() - landTime >= BUFFER_TIME) {
            if (this.currentRotation == 0) {
                this.currentRotation = 3;
            } else {
                this.currentRotation--;
            }
            pivot(this.currentRotation);
        }
    }

    public void rotateRight() {
        if (!hasLanded || System.currentTimeMillis() - landTime >= BUFFER_TIME) {
            if (this.currentRotation == 3) {
                this.currentRotation = 0;
            } else {
                this.currentRotation++;
            }
            pivot(this.currentRotation);
        }
    }

    public boolean checkValidPivot(int rotation) {
        int pivotX = this.cells.get(1).getX();
        int pivotY = this.cells.get(1).getY();
        int[][] nextRotation = shape.getCoordinates(rotation);

        for (int i = 0; i < 4; i++) {
            int cellX = nextRotation[0][i] + pivotX - 1;
            int cellY = nextRotation[1][i] + pivotY;
            if (cellX < 0 || cellX >= board.getWidth() || cellY >= board.getHeight() || (board.getCell(cellX, cellY) != null && (!this.cells.contains(board.getCell(cellX, cellY)) || !board.getCell(cellX, cellY).isActive))) {
                return false;
            }
        }
        return true;
    }

    public void pivot(int rotation) {
        if (!checkValidPivot(rotation)) {
            return;
        }
        int pivotX = this.cells.get(1).getX();
        int pivotY = this.cells.get(1).getY();
        int[][] nextRotation = shape.getCoordinates(rotation);

        for (TetrisCell cell : this.cells) {
            cell.destroy();
        }
        for (TetrisCell cell : this.cells) {
            cell.setX(nextRotation[0][this.cells.indexOf(cell)] + pivotX - 1);
            cell.setY(nextRotation[1][this.cells.indexOf(cell)] + pivotY);
            cell.resetInterpolation();
        }
        for (TetrisCell cell : this.cells) {
            cell.setOnBoard();
        }
    }

    public void deactivate() {
        if (bottomCollision()){
            for (TetrisCell cell : this.cells) {
                cell.updateInterpolatedY();
                cell.isActive = false;
            }
        }
    }

    public void placeOnBoard() {
        for (TetrisCell cell : this.cells) {
            board.setCell(cell.getX(), cell.getY(), cell);
        }
    }

    public boolean hasLanded() {
        return this.hasLanded;
    }

    public long getLandTime() {
        return this.landTime;
    }

    public static long getBufferTime() {
        return BUFFER_TIME;
    }

    private boolean canMove(int moveX, int moveY) {
        for (TetrisCell cell : this.cells) {
            int newX = cell.getX() + moveX;
            int newY = cell.getY() + moveY;
            if (newX < 0 || newX >= board.getWidth() || newY < 0 || newY >= board.getHeight() || (board.isOccupied(newX, newY) && !this.cells.contains(board.getCell(newX, newY)))) {
                return false;
            }
        }
        return true;
    }

    private boolean hasEmptyCellsUnderneath() {
        for (TetrisCell cell : this.cells) {
            int belowY = cell.getY() + 1;
            if (belowY < board.getHeight() && !board.isOccupied(cell.getX(), belowY)) {
                return true;
            }
        }
        return false;
    }
}