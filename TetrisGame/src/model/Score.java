package model;

public class Score {
    private int score;
    private String name; // for gameover logic

    public Score() {
        this.score = 0;
    }
    public void updateScore(int clearedLines, int levelMultiplier){
        this.score += clearedLines * 100 * levelMultiplier;
    }
    public int getScore(){
        return score;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
