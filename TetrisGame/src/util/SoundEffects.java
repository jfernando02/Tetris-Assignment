package util;

import controller.MainFrame;
import controller.MainFrameBase;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffects {
    private final MainFrameBase mainFrame;
    Clip blockPlacement;
    Clip gameOver;
    Clip lineClear;
    Clip menuKeyPress;


    public SoundEffects(MainFrameBase mainFrame) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.mainFrame = mainFrame;
        // get audio, set it and load it
        AudioInputStream blockPlacementAudio = AudioSystem.getAudioInputStream(new File("src/resources/sounds/BlockPlacement.wav").getAbsoluteFile());
        blockPlacement = AudioSystem.getClip();
        blockPlacement.open(blockPlacementAudio);
        addClipListener(blockPlacement);

        AudioInputStream gameOverAudio = AudioSystem.getAudioInputStream(new File("src/resources/sounds/GameOver.wav").getAbsoluteFile());
        gameOver = AudioSystem.getClip();
        gameOver.open(gameOverAudio);
        addClipListener(gameOver);

        AudioInputStream lineClearAudio = AudioSystem.getAudioInputStream(new File("src/resources/sounds/LineClear.wav").getAbsoluteFile());
        lineClear = AudioSystem.getClip();
        lineClear.open(lineClearAudio);
        addClipListener(lineClear);

        AudioInputStream menuKeyPressAudio = AudioSystem.getAudioInputStream(new File("src/resources/sounds/MenuKeyPresses.wav").getAbsoluteFile());
        menuKeyPress = AudioSystem.getClip();
        menuKeyPress.open(menuKeyPressAudio);
        addClipListener(menuKeyPress);
    }

    private void addClipListener(Clip clip) {
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.setFramePosition(0); // Reset the clip to the beginning
            }
        });
    }

    public void playSound(String sound) {
        if (mainFrame.getConfigData().isSoundEffect()) {
            switch (sound) {
                case "blockPlacement":
                    if (blockPlacement.isRunning()) {
                        blockPlacement.setFramePosition(0); // Reset the clip to the start
                    }
                    blockPlacement.start(); // Play the clip from the start
                    break;
                case "gameOver":
                    if (gameOver.isRunning()) {
                        gameOver.setFramePosition(0);
                    }
                    gameOver.start();
                    break;
                case "lineClear":
                    if (lineClear.isRunning()) {
                        lineClear.setFramePosition(0);
                    }
                    lineClear.start();
                    break;
                case "menuKeyPress":
                    if (menuKeyPress.isRunning()) {
                        menuKeyPress.setFramePosition(0);
                    }
                    menuKeyPress.start();
                    break;
            }
        }
    }

}