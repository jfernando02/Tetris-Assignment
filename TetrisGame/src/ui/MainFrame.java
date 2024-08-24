// Main application window for the game (Holds Splash Screen and the Main Panel
// Uses the UIGenerator class to generate the UI components via the panels
package ui;

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
    private int fieldWidth;
    private int fieldHeight;
    private int startLevel = 1;
    private boolean music;
    private boolean soundEffect;
    private boolean aiPlay; // ghost piece
    private boolean extendedMode; // extended mode

    // For scores (and HighScorePanel)
    private int currentScore; // for the current gameplay score which is dynamically updated
    //dynamic array of scores to the high score panel
    private ArrayList<Score> scores = new ArrayList<Score>();

    public MainFrame(String title, int mainWidth, int mainHeight) {
        this.title = title;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        this.fieldWidth = 10; // for FieldPane (default width)
        this.fieldHeight = 20; // for FieldPane (default height)
        //initialise to maintain states for rendering the field and grid
        this.board = new Board(this);
        this.game = new Game(this, board);
        this.renderExecutor = Executors.newSingleThreadScheduledExecutor();
        this.gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();
        //this.level = 1; // default level setting
        this.music = true; // default music setting
        this.soundEffect = true; // default sound effect setting
        this.aiPlay = false; // default ghost piece setting
        this.extendedMode = false; // default extended mode setting
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

        game.setStartLevel(startLevel);
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
        //period now handled by game.getPeriod() (Stefan)
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
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
        System.out.println("Updated game field width: " + fieldWidth);
    }


    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
        System.out.println("Updated game field height: " + fieldHeight);
    }

    // for GamePanel
    public int getStartLevel() {
        return this.startLevel;
    }

    //This level is the STARTING LEVEL set by the configuration panel BEFORE a game starts.
    public void setStartLevel(int level) {
        this.startLevel = level;
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
        return music;
    }

    // TODO: change event listeners to respond to if music if on/off
    public void setMusic(boolean music) {
        this.music = music;
        System.out.println("Updated music setting: " + music);
    }

    // TODO: set event listeners to respond to if sound effects are on/off
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

    // For ghost piece (TODO: validate AI method (Joseph's idea: https://github.com/nuno-faria/tetris-ai)
    public void setAiPlay(boolean aiPlay) {
        this.aiPlay = aiPlay;
        System.out.println("Updated AI play setting: " + aiPlay);
    }

    // For rendering two play panels (nesting fieldPanes) unto the one game panel (TODO: implement
    public boolean isExtendedMode() {
        return extendedMode;
    }

    public void setExtendedMode(boolean extendedMode) {
        this.extendedMode = extendedMode;
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

    // Method to play sound with an option to loop, returns the clip for stoppping
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

}