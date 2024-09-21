package model.gamefactory;
import controller.MainFrame;
import view.panel.GamePanel;


// For Lucas TODO: Lucas: implement sending and receiving gameplay logic to the external server
public class GameExternal extends GameDefault {
    public GameExternal(MainFrame mainFrame, GamePanel gamePanel, String playerName) {
        super(mainFrame, gamePanel, playerName);

        this.player.setPlayerType("External");
    }


}
