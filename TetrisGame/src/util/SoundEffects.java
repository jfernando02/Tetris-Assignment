package util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundEffects {
    private static final Map<String, String> soundFiles = new HashMap<>();

    static {
        soundFiles.put("blockPlacement", "src/resources/sounds/BlockPlacement.wav");
        soundFiles.put("gameOver", "src/resources/sounds/GameOver.wav");
        soundFiles.put("lineClear", "src/resources/sounds/LineClear.wav");
        soundFiles.put("menuKeyPress", "src/resources/sounds/MenuKeyPresses.wav");
    }

    private static Clip createClip(String sound) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filePath = soundFiles.get(sound);
        if (filePath == null) {
            throw new IllegalArgumentException("Unknown sound: " + sound);
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }

    public static void playSound(String sound) {
        try {
            Clip clip = createClip(sound);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}