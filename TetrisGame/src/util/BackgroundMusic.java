package util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusic {
    private Clip music;
    AudioInputStream audio;

    public BackgroundMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //"src/resources/sounds/InGameMusic.wav"
        this.audio = AudioSystem.getAudioInputStream(new File("src/resources/sounds/InGameMusic.wav").getAbsoluteFile());
        this.music = AudioSystem.getClip();
        this.music.open(audio);
    }

    public void playMusic() {
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // to stop audio
    public void stopMusic() {
        if (music != null && music.isRunning()) {
            music.stop();
        }
    }
}
