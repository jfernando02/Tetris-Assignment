package model.gamefactory;

import controller.MainFrame;
import controller.ExternalController;
import model.Player;
import model.TetrisBlock;
import view.panel.GamePanel;

public class GameExternal extends GameDefault {
    private ExternalController externalController;

    private int rotationCount;
    private int xPosition;
    private TetrisBlock lastActiveShape; // Tracker to limit server/client messaging

    public GameExternal(MainFrame mainFrame, GamePanel gamePanel, String playerName, ExternalController externalController) {
        super(mainFrame, gamePanel, playerName);
        this.externalController = externalController;
        this.player.setPlayerType("External");

        this.externalController.setGame(this); //
        this.externalController.run(); // Initial Start of Client/Server Connection
    }

    // Method to update the game state based on server data
    @Override
    public void update(int keyCode) {
        if (activeShape == null) {
//            System.out.println("activeShape == NULL.");
            return;
        }
        // Check if the active shape has changed
        if (activeShape != lastActiveShape) {
//            System.out.println("New active shape detected: " + activeShape);

            // Send game state when new block given
            externalController.sendGameUpdate();
            getRotationCountAndXPosition(); // Fetch the latest rotation count and x position

            // Apply rotation
            if (rotationCount > 0) {
                for (int i = 0; i < rotationCount; i++) {
                    activeShape.rotateRight();
                }
            }

            lastActiveShape = activeShape;
        }

        int currentCol = activeShape.getColumn();
        int colDiff = xPosition - currentCol;

        // Move the active shape
        if (colDiff > 0) {
            activeShape.moveRight();
        } else if (colDiff < 0) {
            activeShape.moveLeft();
        }

        activeShape.softDrop();
    }

    private void getRotationCountAndXPosition() {
        this.rotationCount = externalController.getRotationCount();
        this.xPosition = externalController.getXPosition();

//        System.out.println("Rotation Count: " + rotationCount);
//        System.out.println("X Position: " + xPosition);
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
        this.externalController.disconnect(); // So client can reconnect after starting a new game
    }

}
