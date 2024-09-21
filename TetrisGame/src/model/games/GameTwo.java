package model.games;

import controller.MainFrame;
import model.Board;
import model.Player;
import view.panel.GamePanel;

public class GameTwo extends Game {

    public GameTwo(MainFrame mainFrame, GamePanel gamePanel) {
        super(mainFrame, gamePanel);
        player.setName("Player 2");
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
