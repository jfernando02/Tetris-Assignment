// Game.java
package model.games;

import ai.TetrisAI;
import controller.MainFrame;
import model.*;
import view.panel.GamePanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {
    protected MainFrame mainFrame;
    protected Board board;
    protected TetrisBlock activeShape;
    private TetrisBlock nextShape;
    private int nextShapeIndex;
    protected GamePanel gamePanel;
    protected Clip gameMusic;
    protected boolean playing;
    protected boolean paused = false;
    protected Player player;
    protected TetrisAI ai;
    private boolean training = true;
    protected int period; //period to set the thread timer
    // decreases thread period by 15 every level up
    protected int periodDecr = 15;
    private boolean gameRunning;

    public Game(MainFrame mainFrame, GamePanel gamePanel) {
        this.mainFrame = mainFrame;
        this.gamePanel = gamePanel;
        this.board = new Board(mainFrame, this);
        this.player = new Player("Player1", mainFrame.getConfigData().getStartLevel());
        if(mainFrame.getConfigData().isAiPlay()) {
            this.player.setAI();
            this.ai = new TetrisAI(this);
        }
        this.nextShapeIndex=0;
        this.activeShape = null;
        gameRunning = false;
        spawn();
        this.period = 200 - (player.getLevel()*periodDecr); //starting period, each level will decrease this by 10 (can be changed)
    }


    //method which holds the logic for starting a new game
    public void newGame() {
        if (!gameRunning) {
            this.gameRunning = true;
            this.playing = true;
            this.paused = false;
            this.period = 200 - (player.getLevel()*periodDecr);
            this.gameMusic = mainFrame.playSound("src/resources/sounds/InGameMusic.wav", true);
            System.out.println("Game object says: New game Started at level " + player.getLevel());
        }
    }

    // Start the game or offer user to start a new game if the prior game is over
    public void start() {
        //if game over or the game is paused, ignore start button
        if(gameRunning) {
            System.out.println("Game Object said: Game is already running");
        } else {
            newGame();
        }
    }

    //Is called everytime the play button is selected (will override existing configurations if not set here!)
    public void resetGame() {
        this.board.clearBoard();
        this.gameRunning = false;
        this.activeShape = null;
        this.playing = false;
        this.paused = false;
        this.player = new Player("Player1", mainFrame.getConfigData().getStartLevel());
        if(mainFrame.getConfigData().isAiPlay()){
            this.player.setAI();
        }
        this.period = 200 - (player.getLevel()*periodDecr);
        mainFrame.repaintBoard(); //don't delete or a new game won't render its new state on the fieldPanel in the GamePanel
    }

    //logic for playing whilst "playing = true"
    public void play() {
        if (playing) {
            if (this.activeShape == null) {
                spawn();
                gamePanel.updatePlayPanel();
                activeShape.run(this.board);

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

    // Use old shape as the current shape, and spawn new shape for the next block
    public void spawn() {
        activeShape = nextShape;
        nextShape = mainFrame.getNextBlock(nextShapeIndex+1);
        nextShape.setBoard(board);
        nextShapeIndex++;
    }

    // For safety, not sure if we need it
    private boolean shouldSettle() {
        return System.currentTimeMillis() - activeShape.getLandTime() >= TetrisBlock.getBufferTime();
    }

    // Finalize the shape and place it on the board (for slight landing buffer)
    private void finalizeShape() {
        activeShape.placeOnBoard();
        mainFrame.playSound("src/resources/sounds/BlockPlacement.wav", false);
        checkForLineClear();
        if (isGameOver()) {
            this.playing = false;
            mainFrame.playSound("src/resources/sounds/GameOver.wav", false);
            gameOverPanel();
        }
    }

    // Check for line clear
    protected void checkForLineClear() {
        // Logic to check and clear full lines on the board
        int clearedLines = board.clearCompleteLines();
        if (clearedLines>0) {
            System.out.println("Game Object says: " + clearedLines + " lines cleared");
            player.updateScore(clearedLines);
            //update MainFrame period in case of level up
            this.period = 200 - (player.getLevel()*periodDecr);
            System.out.println("Game Object says: Period set to " + period);
            mainFrame.updateGamePeriod();
            //update PlayPanel
            gamePanel.updatePlayPanel();
        }
    }

    public void gameOverPanel() {
        mainFrame.stopSound(gameMusic);
        this.gameRunning = false;
        if(player.isAI() && training) {
            //Get score and give it to the board game evaluator
            this.ai.getEvaluator().setFinalHighScore(this.player.getScore());
            this.ai.getEvaluator().setFitness("src/ai/DNA.json");
            this.ai = new TetrisAI(this);
            resetGame();
            start();
            return;
        }
        pause();
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
            gamePanel.setStartButtonText("New Game");
            //refocus on game panel
            mainFrame.getGamePanel().requestFocusInWindow();
            dialog.dispose();
        }
    }

    // Determine if game over conditions are met
    public boolean isGameOver() {
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

    // Pauses the game if it's playing, resumes if it's paused
    public void pause() {
        if (paused && !playing) {
            playing = true;
            paused = false;
            // Resume music
            this.gameMusic = mainFrame.playSound("src/resources/sounds/InGameMusic.wav", true);

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
        this.gameMusic = mainFrame.playSound("src/resources/sounds/InGameMusic.wav", true);
        System.out.println("Game Object says: Game resumed");
        playing = true;
        paused = false;

        gamePanel.setPaused(false);
        gamePanel.requestFocusInWindow();
    }


    //Stops the game and offers the user an option to return to the main menu
    public void stop() {
        boolean wasPaused = paused;
        mainFrame.stopSound(gameMusic);
        if (playing) {
            System.out.println("Game paused");
            mainFrame.pauseGame(); // pauses both games if multiplayer
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
            //reset field pane configuration to default (requirement)
            mainFrame.resetFieldPaneConfig();
        } else {
            mainFrame.pauseGame();
            dialog.dispose();
        }
    }

    //Update handles user keyboard input and updates the game state accordingly
    public void update(int keyCode) {
        if (activeShape==null) {
            return;
        }
        if (playing) {
            if(player.isAI() && activeShape!=null){
                dropPiece();
            }
            else{
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
                        activeShape.softDrop();
                        activeShape.softDrop(); //TODO: review down speed logic
                        break;
                }
            }
        }
        switch (keyCode) {
            case KeyEvent.VK_P:
                System.out.println("P key pressed");
                //if paused, set GamePanel pause button to resume
                gamePanel.pauseGame();
                break;
            case KeyEvent.VK_S:
                System.out.println("S key pressed"); //stop
                //toggle sound effect off
                mainFrame.toggleSound();
                break;
            case KeyEvent.VK_M:
                System.out.println("M key pressed"); //mute
                //toggle music off
                mainFrame.toggleMusic();
                break;
        }
    }

    public void dropPiece() {
        // Let the AI decide the best move
        Move bestMove = ai.findBestMove(activeShape);
        if (activeShape==null){
            System.out.println("No active piece");
        }
        // Rotate the piece right
        else if (bestMove.rotation!=activeShape.getCurrentRotation()) {
            activeShape.rotateRight();
        }
        // Move the piece to the best column
        else if (activeShape.getColumn() < bestMove.col) {
            activeShape.moveRight();

        }
        else if (activeShape.getColumn() > bestMove.col) {
            activeShape.moveLeft();
        }
        // Drop the piece
        else {
            activeShape.hardDrop();
        }
    }

    public Board<TetrisCell> getBoard() {
        return board;
    }

    public boolean isPlaying() {
        return playing;
    }

    //update period and return it to the thread assigned to the game
    public int getPeriod() {
        return period;
    }

    public void setStartLevel(int level) {
        resetGame();
        player.setLevel(level);
        this.period = 200 - (player.getLevel()*periodDecr);
        mainFrame.getGameLogicOne().setPeriod(period);
        System.out.println("Game Object says: Level set to " + player.getLevel());
    }

    // For if the game is running (even if paused) so the start button doesn't reset the game
    public boolean isGameRunning() {
        return gameRunning;
    }

    public int getScore() {
        return player.getScore();
    }

    public Clip getPlayingMusic() {
        return gameMusic;
    }

    public boolean isPaused() {
        return paused;
    }

    public TetrisBlock getNextPiece() {
        return nextShape;
    }

    public TetrisBlock getActiveShape() {
        return activeShape;
    }

    public Player getPlayer() {
        return player;
    }
}