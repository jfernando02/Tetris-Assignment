// Main application window for the game (Holds Splash Screen and the Main Panel
// Uses the UIGenerator class to generate the UI components via the panels
package ui;

import java.awt.*;
import java.util.concurrent.Executors;
import model.Board;
import model.Game;
import ui.panel.*;

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
    private GamePanel gamePanel;

    //to manage gameplay state (not executed extra threads)
    private ScheduledExecutorService executor;

    // Configuration settings
    private int fieldWidth;
    private int fieldHeight;
    private int level = 1;
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
        //initialise to maintain states for rendering the field and grid
        this.board = new Board(this);
        this.game = new Game(this, board);
        this.executor = Executors.newSingleThreadScheduledExecutor();
        //this.level = 1; // default level setting
        this.music = true; // default music setting
        this.soundEffect = true; // default sound effect setting
        this.aiPlay = false; // default ghost piece setting
        this.extendedMode = false; // default extended mode setting
        this.gamePanel = null;

        setTitle(this.title);
        setSize(this.mainWidth, this.mainHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //getters and setters
    public void refreshBoard() {
        this.board.refreshBoard(this);
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

    // First state user sees when the game is launched
    public void showSplashScreen() {
        getContentPane().removeAll();
        SplashPanel splashPanel = new SplashPanel(1000, this::showMainPanel);
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
        gamePanel = new GamePanel(this, game);

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


    // >> LOGIC FOR GAME LOOP <<
    public void startGame() {
        if (game.isPlaying()) {
            //game.stop(); // Stop the current game if it's already running
            game.resetGame(); // Reset the game state
        }

        updateGamePeriod(); // Restart the game loop with the current settings
        System.out.println("MainFrame said: New Game Started");
    }

    // Add a method to update the game period (TODO: check and fix speed of level)
    public void updateGamePeriod() {
        //delete prior threads
        executor.shutdownNow();
        executor = Executors.newSingleThreadScheduledExecutor();
        game.start();
        long period = 1000 - (level * 50); // TODO: Adjust the period based on the level: Milliseconds
        executor.scheduleAtFixedRate(() -> { //TODO: Still need to implement level (update) system, just a socket
            if (game.isPlaying()) {
                game.play();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    public void resetGame() {
        this.board = new Board(this); // Reset the board state
        this.game = new Game(this, board); // Reset the game state
        if (gamePanel != null) {
            gamePanel.requestFocusInWindow(); // Refocus on the game panel
        }
    }


    // Modify the stopGame method
    public void stopGame() {
        //stop the playing sound

        game.stop();
        System.out.println("MainFrame said: Game Stopped");
    }


    public void pauseGame() {
        game.pause();
        //refocus on the game panel
        gamePanel.requestFocusInWindow();
    }

    // Important for rendering the current board state in the field pane
    public void repaintBoard() {
        gamePanel.updateField(game.getBoard());
    }

    //Method to get the game to manage it
    public Object getGame() {
        return game;
    }

    //Method to get the game panel so other classes have access to it
    public Component getGamePanel() {
        return gamePanel;
    }

    // Method to update the score MainFrame -> GamePanel -> Game and back
    public void showHighScorePanel() {
        HighScorePanel highScorePanel = new HighScorePanel(this);
        getContentPane().removeAll();
        setContentPane(highScorePanel);
        revalidate();
        repaint();
    }

    // TODO: Validate if extra mainFrame can be resolved for state management
    public MainFrame() {
        MainFrame frame = new MainFrame("Game Title", 800, 600); // Assuming you have a MainFrame constructor with parameters
        HighScorePanel highScoresPanel = new HighScorePanel(frame);
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
        clip.stop();
    }
}
