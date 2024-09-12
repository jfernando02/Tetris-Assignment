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
    GamePanel gamePanel;
    private Clip gameMusic;

    private boolean playing;
    private boolean paused = false;
    private boolean gameOver = false;

    // TODO: Some info for the PlayPanel (milestone 2)
    private Score score;
    private int level;
    private int linesCleared;
    private int numBlocks = 0; //number of blocks spawned

    private int period; //period to set the thread timer
    private int periodDecr = 15;
    private boolean wasStarted;

    public Game(MainFrame mainFrame, Board board) {
        this.mainFrame = mainFrame;
        this.board = board;
        board.setGame(this); //assign the board to the game
        this.activeShape = null;
        this.level = mainFrame.getStartLevel();
        this.linesCleared = 0;
        this.period = 200 - (level*periodDecr); //starting period, each level will decrease this by 10 (can be changed)
        gamePanel = (GamePanel) mainFrame.getGamePanel();
        wasStarted = false;
    }

    //method which holds the logic for starting a new game
    public void newGame() {
        this.playing = true;
        this.paused = false;
        this.score = new Score();
        this.level = mainFrame.getStartLevel(); //starting level
        this.period = 200 - (level*periodDecr);
        this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
        System.out.println("Game object says: New game Started at level " + level);
    }

    // Start the game or offer user to start a new game if the prior game is over
    public void start() {
        //if game over or the game is paused, ignore start button
        if(gameOver) {
            return;
        } else if (isGameOver()) {
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

    //logic for playing whilst "playing = true"
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

    // Spawn new block on the board at the spawn location (in board)
    public void spawn() {
        TetrisBlock newBlock = new TetrisBlock(this.board);
        this.activeShape = newBlock.spawnBlock(numBlocks);
        numBlocks++;
    }

    // Finalize the shape and place it on the board (for slight landing buffer)
    private void finalizeShape() {
        activeShape.placeOnBoard();
        mainFrame.playSound("src/resources.sounds/BlockPlacement.wav", false);
        checkForLineClear();
        if (isGameOver()) {
            this.playing = false;
            mainFrame.playSound("src/resources.sounds/GameOver.wav", false);
            gameOverPanel();
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

    // Determine if game over conditions are met
    private boolean isGameOver() {
        //if any x in the first 3 lines is occupied
        for (int y = 0; y < 3; y++) {
            for (int x = board.getSpawnY(); x < board.getSpawnX() + 4; x++) {
                if (board.getCell(x, y) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    // Check for line clear (TODO: refactor for board.clearCompleteLines() to return the number of lines cleared (for scoring logic))
    private void checkForLineClear() {
        // Logic to check and clear full lines on the board
        this.linesCleared+= board.clearCompleteLines();

        //update level will also update the period for the MainFrame
        updateLevel();
    }

    private void updateLevel() {
        //testing levelling up every 2 cleared lines
        int newLevel = linesCleared / 2 + mainFrame.getStartLevel(); // want start level
        int difference = newLevel - this.level;
        if (difference > 0) {

            //update period
            this.period -= (difference*periodDecr);
            System.out.println("Game Object says: Level up! New level: " + newLevel + " Period: " + period);
            mainFrame.setPeriod(period);
            mainFrame.updateGamePeriod(); //finally works
        }
        this.level = newLevel;
        //update mainframe level for testing, remove if working
        mainFrame.setCurrentLevel(this.level);
    }

    // Pauses the game if it's playing, resumes if it's paused
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

    // Resumes a paused game
    public void resumeGame() {
        this.gameMusic = mainFrame.playSound("src/resources.sounds/InGameMusic.wav", true);
        System.out.println("Game Object says: Game resumed");
        playing = true;
        paused = false;

        gamePanel = (GamePanel) mainFrame.getGamePanel();
        gamePanel.setPaused(false);
        gamePanel.requestFocusInWindow();
    }


    //Stops the game and offers the user an option to terurn to the main menu
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

    //Update handles user keyboard input and updates the game state accordingly
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

    //TODO: socket needs to be implemented for HighScore Panel
    public Score getScoreObject(){
        return score;
    }

    //TODO: Score needs to updated when game over (or potentially for saving a game)
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
        this.wasStarted = false;
        this.playing = false;
        this.numBlocks = 0;
        this.linesCleared = 0;
        this.level = mainFrame.getStartLevel();
        this.period = 200 - (level*periodDecr);
        mainFrame.repaintBoard(); //don't delete or a new game won't render its new state on the fieldPanel in the GamePanel
    }

    public boolean isPaused() {
        return paused;
    }

    //update period and return it to the thread assigned to the game
    public int getPeriod() {
        return period;
    }

    public void setStartLevel(int level) {
        resetGame();
        this.level = level;
        this.period = 200 - (level*periodDecr);
        mainFrame.setPeriod(period);
        System.out.println("Game Object says: Level set to " + level);
    }

}