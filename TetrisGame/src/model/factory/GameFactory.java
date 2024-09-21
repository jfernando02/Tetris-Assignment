package model.factory;
import controller.MainFrame;
import model.games.Game;
import view.panel.GamePanel;

public interface GameFactory {
    Game createGame(MainFrame mainFrame, GamePanel gamePanel);
}
