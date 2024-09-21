package model.games;
import controller.MainFrame;
import model.Move;
import model.Player;
import view.panel.GamePanel;
import ai.TetrisAI;

import javax.swing.*;
import java.awt.event.KeyEvent;


public class GameAI extends GameDefault {
    private TetrisAI ai;
    private boolean training = true;

    public GameAI(MainFrame mainFrame, GamePanel gamePanel) {
        super(mainFrame, gamePanel);
        // Socket for AI play
        this.player.setAI();
        this.ai = new TetrisAI(this);
    }

    @Override
    public void resetGame() {
        this.board.clearBoard();
        this.gameRunning = false;
        this.activeShape = null;
        this.playing = false;
        this.paused = false;
        this.player = new Player("Player1", mainFrame.getConfigData().getStartLevel());

        this.player.setAI();

        this.period = 200 - (player.getLevel()*periodDecr);
        mainFrame.repaintBoard(); //don't delete or a new game won't render its new state on the fieldPanel in the GamePanel
    }

    @Override
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

    @Override
    public void update(int keyCode) {
        if (activeShape==null) {
            return;
        }
        if (playing) {
            if(activeShape!=null) {
                dropPiece();
            }
        }
        if (!mainFrame.getConfigData().isExtendedMode()) {
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
}
