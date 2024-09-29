package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "src/config/scores.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static HighScoreData highScoreData;

    static {
        File highScoreFile = new File(HIGH_SCORE_FILE);
        if (highScoreFile.exists()) {
            highScoreData = loadFromFile();
            //sort high scores for testing
            highScoreData.getScores().sort((o1, o2) -> o2.getScore().compareTo(o1.getScore()));
        } else {
            highScoreData = new HighScoreData();
            saveHighScores(highScoreData);
        }
    }

    public static HighScoreData getHighScoreData() {
        if (highScoreData == null) {
            highScoreData = new HighScoreData();
            saveHighScores(highScoreData);
        }
        return highScoreData;
    }

    public static HighScoreData loadFromFile() {
        File highScoreFile = new File(HIGH_SCORE_FILE);
        if (highScoreFile.exists() && highScoreFile.length() > 0) {
            try (FileReader reader = new FileReader(highScoreFile)) {
                List<HighScoreData.Score> scores = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<HighScoreData.Score>>() {}.getType());
                HighScoreData highScoreData = new HighScoreData();
                highScoreData.setScores(scores);
                return highScoreData;
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return new HighScoreData(); // Return a new HighScoreData object if file doesn't exist or is empty
    }

    public static void saveHighScores(HighScoreData highScores) {
        HighScoreManager.highScoreData = highScores;
        try (FileWriter writer = new FileWriter(HIGH_SCORE_FILE)) {
            gson.toJson(highScores.getScores(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetHighScores() {
        highScoreData = new HighScoreData();
        saveHighScores(highScoreData);
    }
}