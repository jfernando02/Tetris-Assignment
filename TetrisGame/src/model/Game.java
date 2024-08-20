// Manages the game state, updates and overall control
package model;

import ui.MainFrame;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class Game {
    private MainFrame mainFrame;
    private Board board;
    private TetrisBlock activeShape;
    private boolean playing;
    private int score = 0;
    private int level;

    private int spawnX;
    private int spawnY;
    private int numBlocks = 0; //number of blocks spawned

    private Clip gameMusic;

    public Game(MainFrame mainFrame, Board board) {
        this.mainFrame = mainFrame;
        this.board = board;
        board.setGame(this); //assign the board to the game
        this.spawnX = (board.getWidth() / 2)-1;
        this.spawnY = 2;
        this.activeShape = null;
        this.level = mainFrame.getLevel();
    }

    public void start() {
        this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
        this.playing = true;
    }

    public void play() {
        if (playing) {
            if (this.activeShape == null) {
                spawn();
            } else {
                if (activeShape.hasLanded()) {
                    finalizeShape();
                    this.activeShape = null;
                } else {
                    //block thread until activeShape.softDrop() is finished
                    activeShape.softDrop();
                }
            }
        }
        this.mainFrame.repaintBoard();
    }

    public void spawn() {
        TetrisBlock newBlock = new TetrisBlock(this.board);
        this.activeShape = newBlock.spawnBlock(numBlocks);
        numBlocks++;
    }

    // Finalize the shape and place it on the board
    private void finalizeShape() {
        activeShape.placeOnBoard();
        mainFrame.playSound("src/resources.sounds/BlockPlacement.wav", false);
        checkForLineClear();
        if (isGameOver()) {
            mainFrame.playSound("src/resources.sounds/GameOver.wav", false);
            stop();
        }
    }

    private boolean isGameOver() {
        return board.isOccupied(spawnX, spawnY);
    }

    private void checkForLineClear() {
        // Logic to check and clear full lines on the board
        board.clearCompleteLines();
    }

    public void pause() {
        if (playing) {
            mainFrame.stopSound(gameMusic);
            System.out.println("Game paused");
            playing = false;
        }
        JDialog pauseDialog = new JDialog();
        pauseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pauseDialog.setTitle("Pause Game");
        pauseDialog.setSize(200, 200);
        pauseDialog.setVisible(false);
        //centre the dialog
        pauseDialog.setLocationRelativeTo(null);

        int result = JOptionPane.showConfirmDialog(pauseDialog,
                "Game Paused. Do you want to continue?", "Pause Game", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION) {
            this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
            System.out.println("Game resumed");
            playing = true;
            mainFrame.getGamePanel().requestFocusInWindow();
            pauseDialog.dispose();
        } else {
            System.out.println("Game stopped");
            resetGame();
            pauseDialog.dispose();
        }
    }

    public void stop() {
        //TODO: still needs code to handle stopping the game, resetting the board, etc.
        mainFrame.stopSound(gameMusic);
        if (playing) {
            System.out.println("Game paused");
            playing = false;
        }
        //new JDIalog for game over to ask if they're sure if they want to quit
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Stop Game");
        dialog.setSize(200, 200);
        dialog.setVisible(false);
        //centre the dialog
        dialog.setLocationRelativeTo(null);

        //ask if they want to quit the game
        int result = JOptionPane.showConfirmDialog(dialog,
                "Are you sure you want to quit the game?", "Stop Game", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Game stopped");
            resetGame();
            dialog.dispose();
        } else {
            System.out.println("Game resumed");
            this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
            playing = true;
            //refocus on game panel
            mainFrame.getGamePanel().requestFocusInWindow();
            dialog.dispose();
        }
    }

    public int getScore() {
        return score;
    }

    //TODO: fix leveling system, this is just a placeholder
    public void addScore(int addScore) {
        this.score+=(addScore * level);
    }

    public void update(int keyCode) {
        if (activeShape==null) {
            return;
        }
        if (playing) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    System.out.println("Left key pressed");
                    activeShape.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println("Right key pressed");
                    activeShape.moveRight();
                    break;
                case KeyEvent.VK_DOWN:
                    System.out.println("Down key pressed");
                    activeShape.softDrop();
                    break;
                case KeyEvent.VK_UP:
                    System.out.println("Up key pressed");
                    activeShape.rotateRight();
                    break;
                case KeyEvent.VK_CONTROL:
                    System.out.println("Control key pressed");
                    activeShape.rotateLeft();
                    break;
                case KeyEvent.VK_SPACE:
                    System.out.println("Space key pressed"); // hard drop
                    activeShape.hardDrop();
                    break;
            }
            mainFrame.repaintBoard();
        }
        switch (keyCode) {
            case KeyEvent.VK_P:
                System.out.println("P key pressed"); //pause
                pause();
                break;
            case KeyEvent.VK_S:
                System.out.println("S key pressed"); //stop
                stop();
                break;
        }
        mainFrame.repaintBoard();
    }

    public Board<TetrisCell> getBoard() {
        return board;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getLevel() {
        return level;
    }

    public void resetGame() {
        this.board.clearBoard();
        this.activeShape = null;
        this.playing = false;
        this.score = 0;
        this.numBlocks = 0;
        this.level = mainFrame.getLevel();
        mainFrame.repaintBoard();
    }

    public void setPlaying(boolean b) {
        this.playing = b;
    }
}
