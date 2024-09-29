package config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighScoreData {
    private List<Score> scores;

    public HighScoreData() {
        this.scores = new ArrayList<>();
        // add 10 empty scores
        for (int i = 0; i < 10; i++) {
            this.scores.add(new Score("", 0));
        }
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public void addScore(String name, Integer score) {
        this.scores.add(new Score(name, score));
        this.scores.sort(Comparator.comparing(Score::getScore).reversed());
        while (this.scores.size() > 10) {
            this.scores.remove(this.scores.size() - 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Score score : scores) {
            sb.append(score.toString()).append("\n");
        }
        return sb.toString();
    }

    public Boolean isTopTenScore(Integer score) {
        return score > scores.get(scores.size() - 1).getScore();
    }

    public static class Score {
        private String name;
        private Integer score;

        public Score(String name, Integer score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }
    }
}