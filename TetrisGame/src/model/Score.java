package model;

public class Score {
    private int score;
    private String name; // for gameover logic

    public Score(int Score, String name) {
        this.score = Score;
        this.name = name;
    }

    // Getters and Setters
    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
