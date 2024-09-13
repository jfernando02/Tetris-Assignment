package model;

public class Player {
    private String name;
    private int score;
    private int level;
    private int linesCleared;
    private boolean isAI;

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.score = 0;
        this.level = 1;
        this.linesCleared = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLinesCleared(int linesCleared) {
        this.linesCleared = linesCleared;
    }

    public void incrementScore(int increment) {
        score += increment;
    }

    public void incrementLevel() {
        level++;
    }

    public void incrementLinesCleared() {
        linesCleared++;
    }
}
