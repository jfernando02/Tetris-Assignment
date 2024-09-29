package controller;
import config.ConfigData;
import config.ConfigManager;
import config.HighScoreData;
import config.HighScoreManager;
import util.BackgroundMusic;
import util.SoundEffects;
import view.panel.HighScorePanel;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

// Parent class of MainFrame which hosts the skeleton: the background music, sound effects, title, width,
// height, config data, and scores
public abstract class MainFrameBase extends JFrame {
    protected BackgroundMusic backgroundMusic;
    protected SoundEffects soundEffects;
    protected String title;
    protected int mainWidth;
    protected int mainHeight;
    protected ConfigData configData;
    protected HighScoreData highScoreData;
    protected HighScorePanel highScorePanel;


    public MainFrameBase(String title, int mainWidth, int mainHeight) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.title = title;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        this.configData = ConfigManager.getConfigData();
        this.highScoreData = HighScoreManager.getHighScoreData();
        setTitle(this.title);
        setSize(this.mainWidth, this.mainHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            this.backgroundMusic = new BackgroundMusic();
            this.soundEffects = new SoundEffects();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playMusic() {
        if (getConfigData().isMusic()) {
            backgroundMusic.playMusic();
        }
    }

    public void stopMusic() {
        backgroundMusic.stopMusic();
    }

    public void playSound(String sound) {
        if (getConfigData().isSoundEffect()) {
            soundEffects.playSound(sound);
        }
    }

    public ConfigData getConfigData() {
        return configData;
    }


    public void saveConfigData() {
        ConfigManager.saveConfigData(configData);
    }

    public void resetConfigData() {
        ConfigManager.resetConfigData();
        this.configData = ConfigManager.getConfigData();
    }

    public HighScoreData getHighScoreData() {
        return highScoreData;
    }

    public void saveHighScoreData() {
        HighScoreManager.saveHighScores(highScoreData);
    }

    public void resetHighScoreData() {
        HighScoreManager.resetHighScores();
        this.highScoreData = HighScoreManager.getHighScoreData();
    }

    public void resetFieldPaneConfig() {
        ConfigManager.resetFieldPaneConfig();
        this.configData = ConfigManager.getConfigData();
    }


    public void setMusic(boolean music) {
        configData.setMusic(music);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated music setting: " + music);
    }

    public void setSoundEffect(boolean soundEffect) {
        configData.setSoundEffect(soundEffect);
        ConfigManager.saveConfigData(configData);
        System.out.println("Updated sound effect setting: " + soundEffect);
    }

}