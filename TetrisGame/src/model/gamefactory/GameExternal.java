package model.gamefactory;

import controller.MainFrame;
import controller.ExternalController;
import model.Player;
import model.TetrisBlock;
import view.panel.GamePanel;

import javax.swing.*;

public class GameExternal extends GameDefault {
    private ExternalController externalController;

    private boolean serverMessageShown = false;  // Error Notifier on first go

    private int rotationCount;
    private int xPosition;
    private TetrisBlock lastActiveShape;

    public GameExternal(MainFrame mainFrame, GamePanel gamePanel, String playerName, ExternalController externalController) {
        super(mainFrame, gamePanel, playerName);
        this.externalController = externalController;
        this.player.setPlayerType("External");
        this.externalController.setGame(this);
    }

    @Override
    public void gameOverPanel() {
        mainFrame.stopMusic();
        this.gameRunning = false;
        mainFrame.pauseGame();
        if(mainFrame.getHighScoreData().isTopTenScore(player.getScore()) && player.getScore() > 0) {
            if(playerName != null && playerName.length() > 0) {
                // Add the score to the high score data
                mainFrame.getHighScoreData().addScore(player.getName(), player.getScore(), player.getPlayerType());
                // Save new score to scores.json
                mainFrame.saveHighScoreData();
            }
        }

        //new JDIalog for game over to ask if they're sure if they want to quit
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Game Over Panel");
        dialog.setSize(200, 200);
        dialog.setVisible(false);
        //centre the dialog
        dialog.setLocationRelativeTo(null);

        int result;
        String message;
        //ask if they want to quit the game
        if (mainFrame.getConfigData().isExtendedMode()) {
            //message declares who won
            message = "Game Over! " + player.getName() + " won! Do you want to go to the main menu?";
        } else {
            message = "Game Over! Do you want to go to the main menu?";
        }
        result = JOptionPane.showConfirmDialog(dialog, message);

        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Game Object said: going to main menu");
            resetGame();
            dialog.dispose();
            //go to main panel
            gamePanel.resetSize();
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

    // Handling server/client connections, move changes, errors, etc.
    @Override
    public void update(int keyCode) {
        if (activeShape == null) {
            return;
        }
        // Attempt to establish connection with server if there isn't to begin with
        if (!externalController.isConnected()) {
            externalController.run(); // attempt reconnection throughout
            if (!serverMessageShown && !externalController.isConnected()) { // single error message at initial start
                externalController.showServerConnectionError();
                serverMessageShown = true;
                System.out.println("GameExternal Says: Client will attempt to make connections to server throughout the game.");
            }
            return;
        }

        // Retrieve Game Update whenever a new shape appears and apply change
        if (activeShape != lastActiveShape) {
            externalController.sendGameUpdate();
            getRotationCountAndXPosition();

            if (rotationCount > 0) {
                for (int i = 0; i < rotationCount; i++) {
                    activeShape.rotateRight();
                }
            }
            lastActiveShape = activeShape;
        }

        int currentCol = activeShape.getColumn();
        int colDiff = xPosition - currentCol;

        if (colDiff > 0) {
            activeShape.moveRight();
        } else if (colDiff < 0) {
            activeShape.moveLeft();
        }

        activeShape.softDrop();
    }

    // Getter
    private void getRotationCountAndXPosition() {
        this.rotationCount = externalController.getRotationCount();
        this.xPosition = externalController.getXPosition();
    }

    @Override
    public void resetGame() {
        this.board.clearBoard();
        this.gameRunning = false;
        this.activeShape = null;
        this.playing = false;
        this.paused = false;
        this.player = new Player(player.getName(), mainFrame.getConfigData().getStartLevel());
        this.player.setPlayerType("External");
        this.period = 200 - (player.getLevel()*periodDecr);
        mainFrame.repaintBoard(); //don't delete or a new game won't render its new state on the fieldPanel in the GamePanel
        this.externalController.disconnect(); // So clients can connect after starting a new game
    }

}
