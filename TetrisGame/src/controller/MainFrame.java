package controller;

import config.ConfigData;
import config.ConfigManager;
import model.TetrisShape;
import model.games.*;
import model.TetrisBlock;
import model.Score;
import view.panel.GamePanel;
import view.panel.GamePanelMulti;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainFrame extends JFrame {
    private String title;
    private int mainWidth;
    private int mainHeight;

    private GamePanel gamePanel;
    private TetrisShape[] nextPieces;

    private ConfigData configData;
    private ArrayList<Score> scores = new ArrayList<>();

    private MainFramePanels panels;

    // Holds two threads (One for gameplay, one for rendering)
    private MainFrameGameLogic gameLogicOne;
    private Game gameOne;

    // Holds two more threads for extended mode (One for gameLogic, one for rendering)
    private MainFrameGameLogic gameLogicTwo; // For extended mode
    private Game gameTwo; // For extended mode

    public MainFrame(String title, int mainWidth, int mainHeight) {
        this.title = title;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        this.configData = ConfigManager.getConfigData();
        this.panels = new MainFramePanels(this);

        setTitle(this.title);
        setSize(this.mainWidth, this.mainHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Each player draws from the same pool of 1000 pieces (nextPieces)
    public TetrisBlock getNextBlock(int index) {
        int idx = index % 1000; // Wrap around to the beginning of the array
        TetrisBlock block = TetrisBlock.prototype(nextPieces[idx]);
        block.spawnBlock();
        return block;
    }

    // Makes 1000 blocks and stores them in nextPieces
    public void batchSpawnBlocks() {
        this.nextPieces = new TetrisShape[1000];
        for (int i = 0; i < 1000; i++) {
            TetrisShape shape = TetrisShape.getRandomShape();
            nextPieces[i] = shape;
        }
    }

    //Only one game if not extended mode
    public void initSoloGame() {
        batchSpawnBlocks();
        gamePanel = new GamePanel(this);
        if (configData.isAiPlay()) {
            this.gameOne = new GameAI(this, gamePanel);
        } else {
            this.gameOne = new Game(this, gamePanel);
        }

        this.gameLogicOne = new MainFrameGameLogic(this, gameOne);
        gamePanel.setGame(gameOne, null);
    }

    //Two games if extended mode
    public void initMultiplayerGame() {
        // Second player threads
        batchSpawnBlocks();
        this.gamePanel = new GamePanelMulti(this);
        this.gameOne = new GameOne(this, gamePanel);
        this.gameLogicOne = new MainFrameGameLogic(this, gameOne);
        this.gameTwo = new GameTwo(this, gamePanel);
        this.gameLogicTwo = new MainFrameGameLogic(this, gameTwo);
        this.gamePanel.setGame(gameOne, gameTwo);
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
        gameLogicOne.startGame();
    }
    public void startGameTwo() {
        gameLogicTwo.startGame();
    }

    public void updateGamePeriod() {
        //set period
        gameLogicOne.setPeriod(gameOne.getPeriod());
        gameLogicOne.updateGamePeriod();
    }

    public void updateGamePeriodTwo() {
        //set period
        gameLogicTwo.setPeriod(gameTwo.getPeriod());
        gameLogicTwo.updateGamePeriod();
    }

    public void stopGame() {
        // gameLogicOne will also stop gameTwo if extended mode is enabled
        gameLogicOne.stopGame();
        if (gameLogicTwo != null) {
            gameLogicTwo.stopGame();
        }

    }

    public void pauseGame() {
        // gameLogicOne will also pause gameTwo if extended mode is enabled
        gameLogicOne.pauseGame();
        //game logic two pause
        if (gameLogicTwo != null) {
            gameLogicTwo.pauseGame();
        }
    }

    public Game getGameOne() {
        return gameOne;
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

    public MainFrameGameLogic getGameLogicOne() {
        return gameLogicOne;
    }

    public MainFrameGameLogic getGameLogicTwo() {
        return gameLogicTwo;
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
            gamePanel.updateField();
            if (getConfigData().isExtendedMode()) {
                gamePanel.updateFieldTwo(gameTwo.getBoard());
            }
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
            gameOne.getPlayingMusic().stop();
            gamePanel.updateMessageLabel("Music Off");
        } else {
            setMusic(true);
            //play the music
            if(!gameOne.isPaused()) {
                gameOne.getPlayingMusic().loop(Clip.LOOP_CONTINUOUSLY);
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

    public Game getGameTwo() {
        return gameTwo;
    }

    public int numberOfHumanPlayers() {
        return configData.isExtendedMode() ? 2 : 1;
    }




}