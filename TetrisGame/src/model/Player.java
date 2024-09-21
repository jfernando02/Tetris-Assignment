package model;

public class Player {
    private String name;
    private int score;
    private int initialLevel;
    private int level;
    private int linesCleared;
    private String playerType;

    public Player(String name, int initialLevel) {
        this.name = name;
        //initial level read from CONFIG
        this.initialLevel = initialLevel;
        this.level = initialLevel;
        this.score = 0;
        this.linesCleared = 0;
    }

    public void updateScore(int clearedLines){
        linesCleared += clearedLines;
        updateLevel();
        if (clearedLines == 1) {
            score+= 100;
        } else if (clearedLines == 2) {
            score+= 300;
        } else if (clearedLines == 3) {
            score+= 600;
        } else if (clearedLines == 4) {
            score+= 1000;
        }
        System.out.println("Player says: Score: " + score);
        System.out.println("Player says: Level: " + level);

    }
    public void updateLevel(){ level = linesCleared / 10 + initialLevel; } // 10 lines per level

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getInitialLevel() {
        return initialLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getLinesCleared() {
        return linesCleared;
    }

   public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getPlayerType() {
        return playerType;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String s) {
        name = s;
    }

    public void reset() {
        score = 0;
        level = initialLevel;
        linesCleared = 0;
    }
}
