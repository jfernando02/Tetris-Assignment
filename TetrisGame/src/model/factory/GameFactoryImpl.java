package model.factory;

import controller.MainFrame;
import model.games.Game;
import view.panel.GamePanel;

public class GameFactoryImpl implements GameFactory {
    @Override
    public Game createGame(MainFrame mainFrame, GamePanel gamePanel) {
        return new Game(mainFrame, gamePanel);
    }
}