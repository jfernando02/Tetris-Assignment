package controller;

import config.ConfigData;
import config.ConfigManager;
import model.Board;
import model.Game;
import model.TetrisBlock;
import model.Score;
import view.panel.GamePanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class MainFrame extends JFrame {
    private String title;
    private int mainWidth;
    private int mainHeight;
    private Game game;
    private Board board;
    private GamePanel gamePanel;
    private TetrisBlock[] nextPieces;
    private ScheduledExecutorService renderExecutor;
    private ScheduledExecutorService gameLogicExecutor;
    private ConfigData configData;
    private ArrayList<Score> scores = new ArrayList<>();

    private MainFramePanels panels;
    private MainFrameGameLogic gameLogic;

    public MainFrame(String title, int mainWidth, int mainHeight) {
        this.title = title;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        this.configData = ConfigManager.getConfigData();
        this.panels = new MainFramePanels(this);
        this.gameLogic = new MainFrameGameLogic(this);

        setTitle(this.title);
        setSize(this.mainWidth, this.mainHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Each player draws from the same pool of 1000 pieces (nextPieces)
    public TetrisBlock getNextBlock(int index) {
        int idx = index % 1000; // Wrap around to the beginning of the array
        TetrisBlock block = nextPieces[idx].copy();
        block.setBoard(this.board);
        return block;
    }

    // Makes 1000 blocks and stores them in nextPieces
    public void batchSpawnBlocks() {
        this.nextPieces = new TetrisBlock[1000];
        for (int i = 0; i < 1000; i++) {
            TetrisBlock block = new TetrisBlock();
            block.spawnBlock();
            nextPieces[i] = block;
        }
    }


    public void initSoloGame() {
        batchSpawnBlocks();
        this.game = new Game(this);
        this.renderExecutor = Executors.newSingleThreadScheduledExecutor();
        this.gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();
        this.gamePanel = game.getGamePanel();
    }

    public void showGamePanel() { panels.showGamePanel(); }

    public void showSplashScreen() {
        panels.showSplashScreen();
    }

    public void showMainPanel() {
        panels.showMainPanel();
    }

    public void showConfigurePanel() { panels.showConfigurePanel(); }

    public void showHighScorePanel() {
        panels.showHighScorePanel();
    }

    public void startGame() {
        gameLogic.startGame();
    }

    public void updateGamePeriod() {
        //set period
        gameLogic.setPeriod(game.getPeriod());
        gameLogic.updateGamePeriod();
    }

    public void stopGame() {
        gameLogic.stopGame();
    }

    public void pauseGame() {
        gameLogic.pauseGame();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public MainFramePanels getPanels() {
        return panels;
    }

    public MainFrameGameLogic getGameLogic() {
        return gameLogic;
    }

    // Method to play sound with an option to loop, returns the clip for stopping
    public Clip playSound(String soundFile, boolean loop) {
        Clip clip = null;

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (loop) {
                if (getConfigData().isMusic()) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } else if (getConfigData().isSoundEffect()) {
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return clip;
    }

    //stop sound helper function
    public void stopSound(Clip clip) {
        //if the clip is not null
        if (clip != null) {
            clip.stop();
        }
    }

    public void resetFieldPaneConfig() {
        ConfigManager.resetFieldPaneConfig();
        this.configData = ConfigManager.getConfigData();
    }

    // Important for rendering the current board state in the field pane, used by GAME PANEL
    public void repaintBoard() {
        if (gamePanel != null) {
            gamePanel.updateField(game.getBoard());
        } else {
            System.err.println("Error: gamePanel is null");
        }
    }

    public void toggleSound() {
        if (getConfigData().isSoundEffect()) {
            setSoundEffect(false);
            gamePanel.updateMessageLabel("Sound Off");
        } else {
            setSoundEffect(true);
            gamePanel.updateMessageLabel("Sound On");
        }
    }

    public void setSoundEffect(boolean soundEffect) {
        configData.setSoundEffect(soundEffect);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated sound effect setting: " + soundEffect);
    }


    public void toggleMusic() {
        if (getConfigData().isMusic()) {
            configData.setMusic(false);
            //stop the playing sound
            game.getPlayingMusic().stop();
            gamePanel.updateMessageLabel("Music Off");
        } else {
            setMusic(true);
            //play the music
            if(!game.isPaused()) {
                game.getPlayingMusic().loop(Clip.LOOP_CONTINUOUSLY);
            }
            gamePanel.updateMessageLabel("Music On");
        }
    }

    public void setMusic(boolean music) {
        configData.setMusic(music);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated music setting: " + music);
    }

    public void resetConfigData() {
        ConfigManager.resetConfigData();
        this.configData = ConfigManager.getConfigData();
    }
}