// Main application window for the game (Holds Splash Screen and the Main Panel
// Uses the UIGenerator class to generate the UI components via the panels
package ui;


import ui.panel.ConfigurePanel;
import ui.panel.MainPanel;
import ui.panel.SplashPanel;

import java.util.ArrayList; //for scores list

import javax.swing.*;


public class MainFrame extends JFrame {
    private String title;
    private int mainWidth;
    private int mainHeight;

    // Configuration settings
    private int fieldWidth;
    private int fieldHeight;
    private int level;
    private boolean music;
    private boolean soundEffect;
    private boolean aiPlay; // ghost piece
    private boolean extendedMode; // extended mode

    // For scores (and HighScorePanel)
    private int currentScore; // for the current gameplay score which is dynamically updated
    //dynamic array of scores to the high score panel
    private ArrayList<Integer> scores = new ArrayList<Integer>();


    public MainFrame(String title, int mainWidth, int mainHeight) {
        this.title = title;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        this.fieldWidth = 10; // for FieldPane (default width)
        this.fieldHeight = 20; // for FieldPane (default height)

        this.level = 1; // default level setting
        this.music = true; // default music setting
        this.soundEffect = true; // default sound effect setting
        this.aiPlay = false; // default ghost piece setting
        this.extendedMode = false; // default extended mode setting

        setTitle(this.title);
        setSize(this.mainWidth, this.mainHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Method to show the splash screen
    public void showSplashScreen() {
        getContentPane().removeAll();
        SplashPanel splashPanel = new SplashPanel(1000, this::showMainPanel);
        setContentPane(splashPanel);
        revalidate();
        repaint();
    }

    // Method to show the main panel
    public void showMainPanel() {
        getContentPane().removeAll();
        MainPanel mainPanel = new MainPanel(this);
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    // Method to show the configuration panel
    public void showConfigurePanel() {
        ConfigurePanel configurePanel = new ConfigurePanel(this);
        configurePanel.setVisible(true);
    }

    //getters and setters

    public int getMainWidth() {
        return mainWidth;
    }

    public int getMainHeight() {
        return mainHeight;
    }

    //all getters and setters for private variables
    public int getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
        System.out.println("Updated game field width: " + fieldWidth);
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
        System.out.println("Updated game field height: " + fieldHeight);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        System.out.println("Updated game level: " + level);
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
        System.out.println("Updated music setting: " + music);
    }

    public boolean isSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(boolean soundEffect) {
        this.soundEffect = soundEffect;
        System.out.println("Updated sound effect setting: " + soundEffect);
    }

    public boolean isAiPlay() {
        return aiPlay;
    }

    public void setAiPlay(boolean aiPlay) {
        this.aiPlay = aiPlay;
        System.out.println("Updated AI play setting: " + aiPlay);
    }

    public boolean isExtendedMode() {
        return extendedMode;
    }

    public void setExtendedMode(boolean extendedMode) {
        this.extendedMode = extendedMode;
        System.out.println("Updated extended mode setting: " + extendedMode);
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }

    public void addScore(int score) {
        scores.add(score);
    }

    public void clearScores() {
        scores.clear();
    }

    // more if you need
}