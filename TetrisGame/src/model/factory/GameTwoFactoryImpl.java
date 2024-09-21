package model.factory;
import model.games.GameTwo;
import model.games.Game;
import view.panel.GamePanel;
import controller.MainFrame;

public class GameTwoFactoryImpl implements GameFactory {
    @Override
    public Game createGame(MainFrame mainFrame, GamePanel gamePanel) {
        return new GameTwo(mainFrame, gamePanel);
    }
}