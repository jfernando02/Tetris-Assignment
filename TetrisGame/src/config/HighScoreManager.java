package config;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "src/config/scores.json";
    private List<HighScoreData> highScores;

    //high scores list from the JSON file.
    public HighScoreManager() {
        try {
            this.highScores = new Gson().fromJson(new FileReader(HIGH_SCORE_FILE),
                    new TypeToken<List<HighScoreData>>() {
                    }.getType());
        } catch (FileNotFoundException e) {
            highScores = new ArrayList<>();
        }

        if (highScores == null) {
            highScores = new ArrayList<>();
        }
    }

    //put score to the list and re-sort it.
    public void addScore(String player, int score) {
        highScores.add(new HighScoreData(player, score));
        highScores.sort(Comparator.comparing(HighScoreData::getScore).reversed());
        while (highScores.size() > 10) {
            highScores.remove(highScores.size() - 1);
        }
        saveHighScores();
    }

    // Save the high scores
    private void saveHighScores() {
        try (FileWriter writer = new FileWriter(HIGH_SCORE_FILE)) {
            Gson gson = new Gson();
            gson.toJson(highScores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // sort top 10 scores
    public List<HighScoreData> getTopScores() {
        return highScores;
    }
}
