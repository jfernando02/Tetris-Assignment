package model.factory;
import model.games.Game;
import model.games.GameExternal;
import view.panel.GamePanel;
import controller.MainFrame;

public class GameExternalFactoryImpl implements GameFactory {
    @Override
    public Game createGame(MainFrame mainFrame, GamePanel gamePanel) {
        return new GameExternal(mainFrame, gamePanel);
    }
}