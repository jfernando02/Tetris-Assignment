package controller;

import model.*;
import view.panel.*;

public class MainFramePanels {
    private MainFrame mainFrame;

    public MainFramePanels(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void showSplashScreen() {
        mainFrame.getContentPane().removeAll();
        SplashPanel splashPanel = new SplashPanel(1000, mainFrame::showMainPanel);
        mainFrame.setContentPane(splashPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // Show the game panel
    public void showGamePanel() {
        mainFrame.initSoloGame();
        mainFrame.setContentPane(mainFrame.getGamePanel());
    }

    public void showMainPanel() {
        mainFrame.getContentPane().removeAll();
        MainPanel mainPanel = new MainPanel(mainFrame);
        mainFrame.setContentPane(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showConfigurePanel() {
        ConfigurePanel configurePanel = new ConfigurePanel(mainFrame);
        configurePanel.setVisible(true);
    }

    public void showHighScorePanel() {
        HighScorePanel highScorePanel = new HighScorePanel(mainFrame);
        mainFrame.getContentPane().removeAll();
        mainFrame.setContentPane(highScorePanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}