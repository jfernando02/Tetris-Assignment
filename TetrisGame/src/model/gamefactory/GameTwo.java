package model.gamefactory;

import controller.MainFrame;
import view.panel.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GameTwo extends GameDefault {

    public GameTwo(MainFrame mainFrame, GamePanel gamePanel, String playerName) {
        super(mainFrame, gamePanel, playerName);
        player.setName("Player 2");
    }
    @Override
    public void update(int keyCode) {
        if (activeShape==null) {
            return;
        }
        if (playing) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    System.out.println(playerName+": Left key pressed");
                    activeShape.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println(playerName+": Right key pressed");
                    activeShape.moveRight();
                    break;
                case KeyEvent.VK_DOWN:
                    System.out.println(playerName+": Down key pressed");
                    activeShape.softDrop();
                    activeShape.softDrop(); //TODO: review down speed logic
                    break;
                case KeyEvent.VK_UP:
                    System.out.println(playerName+": Up key pressed");
                    activeShape.rotateRight();
                    break;
                case KeyEvent.VK_CONTROL:
                    System.out.println(playerName+": Control key pressed");
                    activeShape.rotateLeft();
                    break;
            }
        }
    }
    @Override
    public void checkForLineClear() {
        // Logic to check and clear full lines on the board
        int clearedLines = board.clearCompleteLines();
        if (clearedLines>0) {
            System.out.println("Game Object says: " + clearedLines + " lines cleared");
            player.updateScore(clearedLines);
            //update MainFrame period in case of level up
            this.period = 200 - (player.getLevel()*periodDecr);
            System.out.println("Game Object says: Period set to " + period);
            mainFrame.updateGamePeriodTwo();
            //update PlayPanel
            gamePanel.updatePlayPanel();
        }
    }

    @Override
    public void setStartLevel(int level) {
        resetGame();
        player.setLevel(level);
        this.period = 200 - (player.getLevel()*periodDecr);
        mainFrame.getGameLogicTwo().setPeriod(period);
        System.out.println("Game Object says: Level set to " + player.getLevel());
    }

    @Override
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

    }

}
