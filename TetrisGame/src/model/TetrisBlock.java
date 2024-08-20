// Defines the shape, movement and rotation of the Tetris blocks
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
        System.out.println("Checking left collision");
        for (TetrisCell cell : this.cells) {
            if (cell.getX() == 0) {
                System.out.println("Left wall detected");
                return true;
            }
            if (board.getCell(cell.getX() - 1, cell.getY()) != null) {
                System.out.println("Cell detected");
                TetrisCell leftCell = board.getCell(cell.getX() - 1, cell.getY());
                if (!leftCell.isActive) {
                    System.out.println("Different id detected");
                    return true;
                }
            }
            //check bottom collision
            if (bottomCollision()) {
                return true;
            }
        }
        return false;
    }

    public boolean rightCollision() {
        System.out.println("Checking right collision");
        for (TetrisCell cell : this.cells) {
            if (cell.getX() == board.getWidth() - 1) {
                System.out.println("Right wall detected");
                return true;
            }
            if (board.getCell(cell.getX() + 1, cell.getY()) != null) {
                System.out.println("Cell detected");
                TetrisCell rightCell = board.getCell(cell.getX() + 1, cell.getY());
                if (!rightCell.isActive) {
                    System.out.println("Different id detected");
                    return true;
                }
            }
            //check bottom collision
            if (bottomCollision()) {
                return true;

            }
        }
        return false;
    }

    public boolean bottomCollision() {
        for (TetrisCell cell : this.cells) {
            if (cell.getY() == board.getHeight() - 1) {
                System.out.println("Bottom barrier detected");
                return true;
            }
            if (board.isOccupied(cell.getX(), cell.getY() + 1) &&
                    !this.cells.contains(board.getCell(cell.getX(), cell.getY() + 1))) {
                TetrisCell bottomCell = board.getCell(cell.getX(), cell.getY() + 1);
                System.out.println("Cell detected");
                if (!bottomCell.isActive) {
                    System.out.println("Different id detected");
                    return true;
                }
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
            } else {
                System.out.println("Error: Coordinates out of bounds");
            }
        }
        for (TetrisCell cell : this.cells) {
            cell.setOnBoard();
        }
    }

    public void softDrop() {
        if (bottomCollision()) {
            deactivate();
        } else {
            moveCells(0, 1);
            for (TetrisCell cell : this.cells) {
                cell.updateInterpolatedY();
            }
        }
    }

    public void hardDrop() {
        while (!bottomCollision()) {
            moveCells(0, 1);
        }
        deactivate();
    }

    public void moveLeft() {
        System.out.println("Attempting to move left");
        if (!leftCollision()) {
            System.out.println("No collision detected, moving left");
            moveCells(-1, 0);
            for (TetrisCell cell : this.cells) {
                cell.resetInterpolation();
            }
        } else {
            System.out.println("Left collision detected");
        }
    }

    public void moveRight() {
        System.out.println("Attempting to move right");
        if (!rightCollision()) {
            System.out.println("No collision detected, moving right");
            moveCells(1, 0);
            for (TetrisCell cell : this.cells) {
                cell.resetInterpolation();
            }
        } else {
            System.out.println("Right collision detected");
        }
    }

    public void rotateLeft() {
        if (this.currentRotation == 0) {
            this.currentRotation = 3;
        } else {
            this.currentRotation--;
        }
        System.out.println("Rotating left");
        pivot(this.currentRotation);
    }

    public void rotateRight() {
        if (this.currentRotation == 3) {
            this.currentRotation = 0;
        } else {
            this.currentRotation++;
        }
        System.out.println("Rotating right");
        pivot(this.currentRotation);
    }

    public boolean checkValidPivot(int rotation) {
        int pivotX = this.cells.get(1).getX();
        int pivotY = this.cells.get(1).getY();
        int[][] nextRotation = shape.getCoordinates(rotation);

        for (int i = 0; i < 4; i++) {
            int cellX = nextRotation[0][i] + pivotX - 1;
            int cellY = nextRotation[1][i] + pivotY;
            if (cellX < 0 || cellX >= board.getWidth() || cellY >= board.getHeight()) {
                return false;
            }
            if (board.getCell(cellX, cellY) != null && (!this.cells.contains(board.getCell(cellX, cellY))
                    || !board.getCell(cellX, cellY).isActive)) {
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
        for (TetrisCell cell : this.cells) {
            cell.isActive = false;
        }
        this.hasLanded = true;
    }

    public void placeOnBoard() {
        for (TetrisCell cell : this.cells) {
            board.setCell(cell.getX(), cell.getY(), cell);
        }
    }

    public boolean hasLanded() {
        return this.hasLanded;
    }
}

/*
TODO:(review the following)
current implementation has Classic Rotation System, check if we want Super Rotation System (SRS):
Classic Rotation System:
* Limited Rotation: In older or classic Tetris games, if a Tetrimino
* is placed next to a wall, rotation might be blocked if any part of
* the block would move outside the playfield bounds. This means you
* often cannot rotate a Tetrimino if it's hugging the wall.

Super Rotation System (SRS):
* Wall Kicks: Modern Tetris games, including those using the SRS, allow for
* "wall kicks." This means that if a Tetrimino cannot rotate because it's next
* to a wall, the game will automatically shift the Tetrimino away from the wall
* to allow the rotation. This makes it possible to rotate Tetriminos even when
* they are hugging the wall.
 */