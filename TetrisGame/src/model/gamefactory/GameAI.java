package model.gamefactory;
import controller.MainFrame;
import model.Move;
import model.Player;
import view.panel.GamePanel;
import ai.TetrisAI;

import javax.swing.*;

// Concrete Product of the Factory Method which inherits from the GameDefault base product class
public class GameAI extends GameDefault {
    private TetrisAI ai;
    private boolean training = false;
    private Move bestMove;
    //private boolean training = true;
    public GameAI(MainFrame mainFrame, GamePanel gamePanel, String playerNumber) {
        super(mainFrame, gamePanel, playerNumber);
        this.ai = new TetrisAI(this);
        this.player.setPlayerType("AI");

    }

    @Override
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

    @Override
    public void update(int keyCode) {
        if (activeShape == null) {
            return;
        }
        if (playing && activeShape != null) {
            bestMove = ai.findBestMove(activeShape);
            // Rotate the piece right
            if (bestMove.rotation != activeShape.getCurrentRotation()) {
                activeShape.rotateRight();
                return;
            }
            // Move the piece to the best column
            if (activeShape.getColumn() < bestMove.col) {
                activeShape.moveRight();
                return;
            }

            if (activeShape.getColumn() > bestMove.col) {
                activeShape.moveLeft();
                return;
            }
            activeShape.softDrop();
            bestMove = null; // Reset the best move after performing it

        }
    }

    @Override
    public void resetGame() {
        this.board.clearBoard();
        this.gameRunning = false;
        this.activeShape = null;
        this.playing = false;
        this.paused = false;
        this.player = new Player(player.getName(), mainFrame.getConfigData().getStartLevel());
        this.player.setPlayerType("AI");
        this.period = 200 - (player.getLevel()*periodDecr);
        mainFrame.repaintBoard(); //don't delete or a new game won't render its new state on the fieldPanel in the GamePanel
    }

    // Ai needs a different game over checker
    @Override
    public boolean isGameOver() {
        // Check if any of the cells of the next shape are already occupied on the board
        int[][] cells = nextShape.getShape();
        for (int i = 0; i < cells.length; i++) {
            int cellX = cells[0][i] + this.board.getSpawnX();
            int cellY = cells[1][i] + this.board.getSpawnY();
            if(board.getCell(cellX, cellY)!=null){
                return true;
            }
        }
        return false;
    }

    @Override
    public void gameOverPanel() {
        mainFrame.stopMusic();
        this.gameRunning = false;
        mainFrame.pauseGame();
        if(training) {
            //Get score and give it to the board game evaluator
            this.ai.getEvaluator().setFinalHighScore(this.player.getScore());
            this.ai.getEvaluator().setFitness("src/ai/DNA.json");
            this.ai = new TetrisAI(this);
            resetGame();
            start();
            return;
        }

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
        mainFrame.gameOverLoser(this);
    }


}
