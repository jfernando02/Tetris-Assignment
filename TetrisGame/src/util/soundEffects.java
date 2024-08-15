// File: SoundUtils.java
package util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class soundEffects {
    public static void playSound(String soundFile) {
        try {
            File soundPath = new File(soundFile);
            AudioInputStream audio = AudioSystem.getAudioInputStream(soundPath);
            Clip soundEffect = AudioSystem.getClip();
            soundEffect.open(audio);
            soundEffect.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
