package model.factory;
import model.games.GameAI;
import view.panel.GamePanel;
import controller.MainFrame;
import model.games.Game;

public class GameAIFactoryImpl implements GameFactory {
    @Override
    public Game createGame(MainFrame mainFrame, GamePanel gamePanel) {
        return new GameAI(mainFrame, gamePanel);
    }
}