package model.games;

import controller.MainFrame;
import view.panel.GamePanel;

import java.awt.event.KeyEvent;

public class GameTwo extends GameDefault {

    public GameTwo(MainFrame mainFrame, GamePanel gamePanel) {
        super(mainFrame, gamePanel);
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
        // Does nothing, as we're making the parent
        // method do all the LABOUR
    }
}
