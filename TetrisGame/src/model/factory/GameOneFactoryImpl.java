package model.factory;
import model.games.GameOne;
import model.games.Game;
import view.panel.GamePanel;
import controller.MainFrame;

public class GameOneFactoryImpl implements GameFactory {
    @Override
    public Game createGame(MainFrame mainFrame, GamePanel gamePanel) {
        return new GameOne(mainFrame, gamePanel);
    }
}