package controller;

import config.HighScoreManager;
import view.panel.*;

import javax.swing.*;

public class PanelsController {
    private MainFrame mainFrame;

    public PanelsController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void showSplashScreen() {
        mainFrame.getContentPane().removeAll();
        SplashPanel splashPanel = new SplashPanel(1000, mainFrame::showMainPanel);
        mainFrame.setContentPane(splashPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Show the single or multiplayer game panel
    public void showGamePanel() {
        if (mainFrame.getConfigData().isExtendedMode()) {
            mainFrame.initMultiplayerGame();
        } else {
            mainFrame.initSoloGame();
        }
        mainFrame.setContentPane(mainFrame.getGamePanel());
    }

    public void showMainPanel() {
        mainFrame.getContentPane().removeAll();
        MainPanel mainPanel = new MainPanel(mainFrame);
        mainFrame.setContentPane(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Only `showConfigurePanel`
    public void showConfigurePanel() {
        ConfigurePanel configurePanel = new ConfigurePanel(mainFrame);
        configurePanel.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        configurePanel.setVisible(true);
    }

    public void showHighScorePanel() {
        mainFrame.getContentPane().removeAll();
        HighScoreManager highScoreManager = new HighScoreManager();

        // method to communicate instance of HighScoreManager to HighScorePanel.
        HighScorePanel highScorePanel = new HighScorePanel(mainFrame);

        mainFrame.setContentPane(highScorePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}