package model.games;

import controller.MainFrame;
import view.panel.GamePanel;

//factory creator, will create the total number of games and return them
public class GameFactory {
    // Game types: "None", "Human", "AI", "External"
    //Pick the type of player for which to construct the game (left or right side of the game panel)
    public GameDefault createGame(MainFrame mainFrame, GamePanel gamePanel, int player) {
        if (mainFrame.getConfigData().isExtendedMode()) {
            if (player == 1) {
                switch(mainFrame.getConfigData().getPlayerOneType()) {
                    case "Human":
                        if (mainFrame.getConfigData().getPlayerTwoType().equals("Human")) {
                            return new GameOne(mainFrame, gamePanel);
                        } else {
                            return new GameDefault(mainFrame, gamePanel);
                        }
                    case "AI":
                        return new GameAI(mainFrame, gamePanel);
                    case "External":
                        return new GameExternal(mainFrame, gamePanel);
                    default :
                        throw new IllegalArgumentException("Invalid player type");
                }
            }
            if (player == 2) {
                switch(mainFrame.getConfigData().getPlayerTwoType()) {
                    case "Human":
                        return new GameTwo(mainFrame, gamePanel);
                    case "AI":
                        return new GameAI(mainFrame, gamePanel);
                    case "External":
                        return new GameExternal(mainFrame, gamePanel);
                    default :
                        throw new IllegalArgumentException("Invalid player type");
                }
            }
        } else {
            if (player == 1) {
                switch(mainFrame.getConfigData().getPlayerOneType()) {
                    case "Human":
                        return new GameDefault(mainFrame, gamePanel);
                    case "AI":
                        return new GameAI(mainFrame, gamePanel);
                    case "External":
                        return new GameExternal(mainFrame, gamePanel);
                    default :
                        throw new IllegalArgumentException("Invalid player type");
                }
            }
        }
        return new GameDefault(mainFrame, gamePanel);
    }
}
