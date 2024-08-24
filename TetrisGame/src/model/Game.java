// Game.java
package model;

import ui.MainFrame;
import ui.panel.GamePanel;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game {
    private MainFrame mainFrame;
    private Board board;
    private TetrisBlock activeShape;
    private boolean playing;
    private boolean paused = false;
    private boolean gameOver = false;
    private Score score;
    //private int score = 0;
    private int level;
    GamePanel gamePanel;
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
        gamePanel = (GamePanel) mainFrame.getGamePanel();
    }

    public void newGame() {
        this.playing = true;
        this.score = new Score();
        this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
    }

    public void start() {
        //if game over or the game is paused, ignore start button
        if(gameOver) {
            return;
        }
        if(isGameOver()) {
            gameOver = true;
            //new panel asking if they want to play a new game
            JDialog dialog = new JDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setTitle("New Game Panel");
            dialog.setSize(200, 200);
            dialog.setVisible(false);
            //centre the dialog
            dialog.setLocationRelativeTo(null);

            //ask if they want to play a new game
            int result = JOptionPane.showConfirmDialog(dialog,
                    "Do you want to play a new game?", "New Game", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                System.out.println("Game Object said: starting new game");
                gameOver = false;
                resetGame();
                start();
            } else {
                System.out.println("Game Object said: going to main menu");
                resetGame();
                dialog.dispose();
                gameOver = false;
                mainFrame.showMainPanel();
            }
        } else {
            newGame();
        }

    }

    public void play() {
        if (playing) {
            if (this.activeShape == null) {
                spawn();
            } else {
                if (activeShape.hasLanded() && shouldSettle() && activeShape.bottomCollision()) {
                    finalizeShape();
                    this.activeShape = null;
                } else {
                    activeShape.softDrop();
                }
            }
        }
    }

    // For safety, not sure if we need it
    private boolean shouldSettle() {
        return System.currentTimeMillis() - activeShape.getLandTime() >= TetrisBlock.getBufferTime();
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
            this.playing = false;
            mainFrame.playSound("src/resources.sounds/GameOver.wav", false);
            gameOverPanel();
            //block start button

        }
    }

    public void gameOverPanel() {
        mainFrame.stopSound(gameMusic);
        //new JDIalog for game over to ask if they're sure if they want to quit
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Game Over Panel");
        dialog.setSize(200, 200);
        dialog.setVisible(false);
        //centre the dialog
        dialog.setLocationRelativeTo(null);

        //ask if they want to quit the game
        int result = JOptionPane.showConfirmDialog(dialog,
                "Game Over! Do you want to go to the main menu?", "Game Over", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Game Object said: going to main menu");
            resetGame();
            dialog.dispose();
            //go to main panel
            mainFrame.showMainPanel();
        } else {
            System.out.println("Game Object said: staying in game");
            //refocus on game panel
            mainFrame.getGamePanel().requestFocusInWindow();
            dialog.dispose();
        }
    }


    private boolean isGameOver() {
        //if any x in the first 3 lines is occupied
        for (int y = 0; y < 3; y++) {
            for (int x = spawnX; x < spawnX + 4; x++) {
                if (board.getCell(x, y) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkForLineClear() {
        // Logic to check and clear full lines on the board
        board.clearCompleteLines();
    }

    public void pause() {
        gamePanel = (GamePanel) mainFrame.getGamePanel();
        if (paused && !playing) {
            playing = true;
            paused = false;
            // Resume music
            this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
            gamePanel.setPaused(false);
            return;
        }
        if (playing) {
            mainFrame.stopSound(gameMusic);
            System.out.println("Game Object says: Game paused");
            playing = false;
            paused = true;
            gamePanel.setPaused(true);
        } else {
            return;
        }

        // Create a JOptionPane for the pause message without any buttons
        JOptionPane pane = new JOptionPane(
                "Game Paused! (Press P to resume)",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{}
        );

        // Create a JDialog from the JOptionPane and set it to be non-modal
        JDialog dialog = pane.createDialog(mainFrame, "Pause Game");
        dialog.setModal(false);

        // Add a KeyListener to the JDialog to listen for the "P" key press
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_P) {
                    System.out.println("Game Object says: P key pressed, resuming game...");
                    resumeGame();
                    dialog.dispose();  // Close the dialog when P is pressed
                }
            }
        });

        // Make sure the dialog is focusable and requests focus
        dialog.setFocusable(true);
        dialog.requestFocusInWindow();

        // Show the dialog
        dialog.setVisible(true);

        // Use a Timer to close the dialog after 1 second
        new javax.swing.Timer(1000, e -> dialog.dispose()).start();

        // Refocus on the game panel after closing the dialog
        mainFrame.getGamePanel().requestFocusInWindow();

        // If pressing P again, resume game
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_P) {
                    System.out.println("Game Object says: P key pressed, resuming game...");
                    resumeGame();
                }
            }
        });
    }

    public void resumeGame() {
        this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
        System.out.println("Game Object says: Game resumed");
        playing = true;
        paused = false;

        gamePanel = (GamePanel) mainFrame.getGamePanel();
        gamePanel.setPaused(false);
        gamePanel.requestFocusInWindow();
    }


    //Stops the game and goes back to the main menu
    public void stop() {
        boolean wasPaused=isPaused();
        mainFrame.stopSound(gameMusic);
        if (playing) {
            System.out.println("Game paused");
            playing = false;
            paused = true;
            GamePanel gamePanel = (GamePanel) mainFrame.getGamePanel();
            gamePanel.setPaused(true);
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
                "Are you sure you want to quit the game and go to the main menu?", "Stop Game", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Game stopped");
            resetGame();
            dialog.dispose();
            //go to main panel
            mainFrame.showMainPanel();
        } else {
            //else refocus on
            mainFrame.getGamePanel().requestFocusInWindow();
            if(!wasPaused){
                resumeGame();
            }
            dialog.dispose();
        }
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
                    activeShape.softDrop(); //TODO: review down speed logic
                    mainFrame.repaintBoard();
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
                System.out.println("P key pressed");
                mainFrame.pauseGame();
                break;
            case KeyEvent.VK_S:
                System.out.println("S key pressed"); //stop
                stop();
                break;
        }
        //mainFrame.repaintBoard();
    }

    public Score getScoreObject(){
        return score;
    }
    public void updateScoreObject(int clearedLines){
        score.updateScore(clearedLines, this.level);
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
        //this.score = 0;
        this.numBlocks = 0;
        this.level = mainFrame.getLevel();
        mainFrame.repaintBoard(); //don't delete
    }

    public void setPlaying(boolean b) {
        this.playing = b;
    }

    public boolean isPaused() {
        return paused;
    }
}