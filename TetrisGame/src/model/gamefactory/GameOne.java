package model.gamefactory;

import controller.MainFrame;
import view.panel.GamePanel;

import java.awt.event.KeyEvent;

public class GameOne extends GameDefault {

    public GameOne(MainFrame mainFrame, GamePanel gamePanel, String playerName) {
        super(mainFrame, gamePanel, playerName);
    }

    // If second player is a real player:
    @Override
    //Update handles user keyboard input and updates the game state accordingly
    public void update(int keyCode) {
        if (activeShape==null) {
            return;
        }
        if (playing) {
            switch (keyCode) {
                //if comma is pressed
                case KeyEvent.VK_COMMA:
                    System.out.println("Player one: Comma key pressed");
                    activeShape.moveLeft();
                    break;
                // if period is pressed
                case KeyEvent.VK_PERIOD:
                    System.out.println("Player one: Period key pressed");
                    activeShape.moveRight();
                    break;
                //if space is pressed
                case KeyEvent.VK_SPACE:
                    System.out.println("Player one: Space key pressed");
                    activeShape.softDrop();
                    activeShape.softDrop(); //TODO: review down speed logic
                    break;
                //if L is pressed
                case KeyEvent.VK_L:
                    System.out.println("Player one: L key pressed");
                    activeShape.rotateRight();
                    break;
            }
            mainFrame.repaintBoard();
        }
        //placeholder (TODO: Stefan: implement player types in config)
        if (mainFrame.numberOfHumanPlayers() == 2) {
            return;
        }
        switch (keyCode) {
            case KeyEvent.VK_P:
                System.out.println("P key pressed");
                //if paused, set GamePanel pause button to resume
                //mainFrame.pauseGame();
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
