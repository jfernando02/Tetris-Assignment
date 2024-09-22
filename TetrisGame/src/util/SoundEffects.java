package util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffects {
    Clip blockPlacement;
    Clip gameOver;
    Clip lineClear;
    Clip menuKeyPress;

    public SoundEffects() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
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
        switch(sound) {
            case "blockPlacement":
                blockPlacement.start();
                break;
            case "gameOver":
                gameOver.start();
                break;
            case "lineClear":
                lineClear.start();
                break;
            case "menuKeyPress":
                menuKeyPress.start();
                break;
        }
    }
}