package util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class backgroundMusic {
    private Clip music;

    public void playMusic(String filePath) {
        try {
            // Get the Audio File and load it
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            music = AudioSystem.getClip();
            music.open(audio);

            // Loop
            music.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // to stop audio
    public void stopMusic() {
        if (music != null && music.isRunning()) {
            music.stop();
        }
    }
}
