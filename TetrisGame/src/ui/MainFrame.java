package ui;

import config.ConfigData;
import config.ConfigManager;

import java.awt.*;
import java.util.concurrent.Executors;

import model.Score;
import ui.panel.*;
import model.Board;
import model.Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.util.ArrayList; //for scores list
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class MainFrame extends JFrame {
    private String title;
    private int mainWidth;
    private int mainHeight;
    // Game state
    private Game game;
    private Board board;
    private ui.panel.GamePanel gamePanel;

    //to manage gameplay state (not executed extra threads)
    private ScheduledExecutorService renderExecutor;
    private ScheduledExecutorService gameLogicExecutor;
    private volatile long period; //will need two periods for multiplayer, each handling different speeds according to level
    // Configuration settings
    private ConfigData configData;

    // For scores (and HighScorePanel)
    private int currentScore; // for the current gameplay score which is dynamically updated
    //dynamic array of scores to the high score panel
    private ArrayList<Score> scores = new ArrayList<Score>();

    public MainFrame(String title, int mainWidth, int mainHeight) {
        this.title = title;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        // Loads the configuration data (Stefan: Milestone 2 update)
        this.configData = ConfigManager.getConfigData();
        //initialise to maintain states for rendering the field and grid
        this.board = new Board(this);
        this.game = new Game(this, board);
        this.renderExecutor = Executors.newSingleThreadScheduledExecutor();
        this.gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();
        this.gamePanel = new GamePanel(this, game);

        setTitle(this.title);
        setSize(this.mainWidth, this.mainHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // --------------------- METHODS FOR SHOWING PANELS --------------------- START
    // First state user sees when the game is launched
    public void showSplashScreen() {
        getContentPane().removeAll();
        ui.panel.SplashPanel splashPanel = new ui.panel.SplashPanel(1000, this::showMainPanel);
        setContentPane(splashPanel);
        revalidate();
        repaint();
    }

    // For play button to show the gamePanel
    public void showGamePanel() {
        if (game == null) {
            //new board
            game = new Game(this, board);
        }
        gamePanel = new ui.panel.GamePanel(this, game);
        //repaint TODO: check if we need this
        gamePanel.repaint();
        // Request focus for the game panel to ensure it receives key events
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        setContentPane(gamePanel);
        revalidate();
        repaint();
    }

    // To transition from the splash screen to the main panel
    public void showMainPanel() {
        getContentPane().removeAll();
        ui.panel.MainPanel mainPanel = new ui.panel.MainPanel(this);
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    // Method to show the configuration panel
    public void showConfigurePanel() {
        ui.panel.ConfigurePanel configurePanel = new ui.panel.ConfigurePanel(this);
        configurePanel.setVisible(true);
    }

    // Method to update the score MainFrame -> GamePanel -> Game and back
    public void showHighScorePanel() {
        HighScorePanel highScorePanel = new HighScorePanel(this);
        getContentPane().removeAll();
        setContentPane(highScorePanel);
        revalidate();
        repaint();
    }
    // --------------------- METHODS FOR SHOWING PANELS --------------------- END

    //--------------------- METHODS FOR THE GAME LOGIC --------------------- START
    // Start a game
    public void startGame() {
        System.out.println("MainFrame said: New Game Started");

        game.setStartLevel(configData.getStartLevel());
        runGamePeriod();
    }

    // Add a method to update the game period
    private void runGamePeriod() {
        game.resetGame();
        // Ensure the previous executors are properly shut down
        while (!renderExecutor.isShutdown()) {
            renderExecutor.shutdownNow();
        }
        while (!gameLogicExecutor.isShutdown()) {
            gameLogicExecutor.shutdownNow();
        }
        renderExecutor = Executors.newSingleThreadScheduledExecutor();
        gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();
        game.start();
        //Thread period now handled by game.getPeriod() (Stefan)
        this.period = game.getPeriod(); //starting period
        updateGamePeriod();
    }

    // Update the game period
    public void updateGamePeriod() {
        System.out.println("MainFrame says: Updating period to " + this.period);

        // Shut down the existing gameLogicExecutor
        if (!gameLogicExecutor.isShutdown()) {
            gameLogicExecutor.shutdownNow();
        }

        // Create a new gameLogicExecutor with the updated period
        gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();

        // Ensure renderExecutor continues to handle rendering
        renderExecutor.scheduleAtFixedRate(() -> {
            if (game.isPlaying()) {
                SwingUtilities.invokeLater(() -> {
                    gamePanel.repaint();
                });
            }
        }, 0, period / 10, TimeUnit.MILLISECONDS);

        gameLogicExecutor.scheduleAtFixedRate(() -> {
            if (game.isPlaying()) {
                game.play();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }


    // Modify the stopGame method
    public void stopGame() {
        //stop the playing sound
        game.stop();
        System.out.println("MainFrame said: Game Stopped");
    }


    public void pauseGame() {
        game.pause();
        gamePanel.requestFocusInWindow();
    }

    //for the configure panel (not game logic which has its own reset logic)
    public void resetGameConfig() {
        this.board = new Board(this); // Reset the board state
        this.game = new Game(this, board); // Reset the game state
        if (gamePanel != null) {
            gamePanel.requestFocusInWindow(); // Refocus on the game panel
        }
    }

    //--------------------- METHODS FOR THE GAME LOGIC --------------------- STOP

    //Refresh the board dimensions when the field dimensions are updated by the CONFIGURATION PANEL
    public void refreshBoard() {
        this.board.refreshBoard(this);
    }

    // Important for rendering the current board state in the field pane, used by GAME PANEL
    public void repaintBoard() {
        if (gamePanel != null) {
            gamePanel.updateField(game.getBoard());
        } else {
            System.err.println("Error: gamePanel is null");
        }
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    //all getters and setters for private variables
    public int getFieldWidth() {
        return configData.getFieldWidth();
    }

    public int getFieldHeight() {
        return configData.getFieldHeight();
    }

    public void setFieldWidth(int fieldWidth) {
        configData.setFieldWidth(fieldWidth);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated game field width: " + fieldWidth);
    }

    public void setFieldHeight(int fieldHeight) {
        configData.setFieldHeight(fieldHeight);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated game field height: " + fieldHeight);
    }

    // for GamePanel
    public int getStartLevel() {
        System.out.println("MainFrame says: Getting start level: " + configData.getStartLevel());
        return configData.getStartLevel();
    }

    //This level is the STARTING LEVEL set by the configuration panel BEFORE a game starts.
    public void setStartLevel(int level) {
        configData.setStartLevel(level);
        ConfigManager.saveConfigData(configData);
        game.setStartLevel(level);
        //set period of executor
        this.period = game.getPeriod();
        System.out.println("MainFrame says: Updated game level: " + level + " with period: " + period);
    }

    //set current level of a game to the GamePanel
    public void setCurrentLevel(int level) {
        gamePanel.updateLevelLabel(level);
    }

    public boolean isMusic() {
        return configData.isMusic();
    }

    // TODO: change event listeners to respond to if music if on/off
    public void setMusic(boolean music) {
        configData.setMusic(music);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated music setting: " + music);
    }

    // TODO: set event listeners to respond to if sound effects are on/off
    public boolean isSoundEffect() {
        return configData.isSoundEffect();
    }

    public void setSoundEffect(boolean soundEffect) {
        configData.setSoundEffect(soundEffect);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated sound effect setting: " + soundEffect);
    }

    public boolean isAiPlay() {
        return configData.isAiPlay();
    }

    // For ghost piece (TODO: validate AI method (Joseph's idea: https://github.com/nuno-faria/tetris-ai)
    public void setAiPlay(boolean aiPlay) {
        configData.setAiPlay(aiPlay);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated AI play setting: " + aiPlay);
    }

    // For rendering two play panels (nesting fieldPanes) unto the one game panel (TODO: implement
    public boolean isExtendedMode() {
        return configData.isExtendedMode();
    }

    public void setExtendedMode(boolean extendedMode) {
        configData.setExtendedMode(extendedMode);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated extended mode setting: " + extendedMode);
    }

    //Method to get the game to manage it
    public Object getGame() {
        return game;
    }

    //Method to get the game panel so other classes have access to it
    public Component getGamePanel() {
        return gamePanel;
    }

    // Method to play sound with an option to loop, returns the clip for stopping
    public Clip playSound(String soundFile, boolean loop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //stop sound helper function
    public void stopSound(Clip clip) {
        //if the clip is not null
        if (clip != null) {
            clip.stop();
        }
    }

    // For config data (Stefan: Milestone 2)
    public ConfigData getConfigData() {
        return configData;
    }

    public void resetConfigData() {
        ConfigManager.resetConfigData();
        this.configData = ConfigManager.getConfigData();
    }
}