package model.gamefactory;

import controller.MainFrame;
import view.panel.GamePanel;
import controller.ExternalController;

//factory creator, will create the total number of games and return them
public class GameFactory {
    //Pick the type of player for which to construct the game (left or right side of the game panel)
    public Game createGame(MainFrame mainFrame, GamePanel gamePanel, int player) {

        if (mainFrame.getConfigData().isExtendedMode()) {
            if (player == 1) {
                switch(mainFrame.getConfigData().getPlayerOneType()) {
                    case "Human":
                        if (mainFrame.getConfigData().getPlayerTwoType().equals("Human")) {
                            return new GameOne(mainFrame, gamePanel, "Player 1");
                        } else {
                            return new GameDefault(mainFrame, gamePanel, "Player 1");
                        }
                    case "AI":
                        return new GameAI(mainFrame, gamePanel, "AI Player 1");
                    case "External":
                        return new GameExternal(mainFrame, gamePanel, "Ext Player 1", new ExternalController(mainFrame));
                    default:
                        throw new IllegalArgumentException("Invalid player type");
                }
            }
            if (player == 2) {
                switch(mainFrame.getConfigData().getPlayerTwoType()) {
                    case "Human":
                        return new GameTwo(mainFrame, gamePanel, "Player 2");
                    case "AI":
                        return new GameAI(mainFrame, gamePanel, "AI Player 2");
                    case "External":
                        return new GameExternal(mainFrame, gamePanel, "Ext Player 2", new ExternalController(mainFrame));
                    default:
                        throw new IllegalArgumentException("Invalid player type");
                }
            }
        } else {
            if (player == 1) {
                switch(mainFrame.getConfigData().getPlayerOneType()) {
                    case "Human":
                        return new GameDefault(mainFrame, gamePanel, "Player");
                    case "AI":
                        return new GameAI(mainFrame, gamePanel, "AI Player");
                    case "External":
                        return new GameExternal(mainFrame, gamePanel, "Ext Player", new ExternalController(mainFrame));
                    default:
                        throw new IllegalArgumentException("Invalid player type");
                }
            }
        }
        return new GameDefault(mainFrame, gamePanel, "Player");
    }
}
