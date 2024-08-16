package model;

public class Score {
    private int score;
    private int level;
    private int rowsCompleted;
    private int comboMultiplier;
    private int highScore;
    private boolean isGameOver;
    
    public Score() {
        this.score = 0;
        this.level = 1;
        this.rowsCompleted = 0;
        this.comboMultiplier = 1;
        this.highScore = 0;
        this.isGameOver = false;
    }
    
    public void addRowsCompleted(int rows) {
        if (rows > 0 && rows <= 4) {
            rowsCompleted += rows;
            
            int baseScore = 0;
            switch (rows) {
                case 1:
                    baseScore = 40 * level;
                    break;
                case 2:
                    baseScore = 100 * level;
                    break;
                case 3:
                    baseScore = 300 * level;
                    break;
                case 4:
                    baseScore = 1200 * level;
                    break;
            }
            score += baseScore * comboMultiplier;
            
            comboMultiplier++;
            
            if (rowsCompleted >= 10 * level) {
                levelUp();
            }
            
            if (score > highScore) {
                highScore = score;
            }
        } else {
            comboMultiplier = 1;
        }
    }
    
    private void levelUp() {
        level++;
    }
    
    public void reset() {
        this.score = 0;
        this.level = 1;
        this.rowsCompleted = 0;
        this.comboMultiplier = 1;
        this.isGameOver = false;
    }
    
    public void gameOver() {
        isGameOver = true;
        if (score > highScore) {
            highScore = score;
        }
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getRowsCompleted() {
        return rowsCompleted;
    }

    public int getHighScore() {
        return highScore;
    }
    
    public int getComboMultiplier() {
        return comboMultiplier;
    }

    @Override
    public String toString() {
        return "Score: " + score + ", Level: " + level + ", Rows Completed: " + rowsCompleted +
                ", Multiplier: " + comboMultiplier + ", High Score: " + highScore +
                ", Game Over: " + isGameOver;
    }
}
