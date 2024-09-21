// src/config/ConfigData.java
package config;

public class ConfigData {
    private int fieldWidth;
    private int fieldHeight;
    private int startLevel;
    private boolean music;
    private boolean soundEffect;

    private String playerOneType;
    private String playerTwoType;

    // Constructor with default data
    public ConfigData() {
        this.fieldWidth = 10;
        this.fieldHeight = 20;
        this.startLevel = 1;
        this.music = true;
        this.soundEffect = true;

        this.playerOneType = "Human";
        this.playerTwoType = "None";
    }

    // Getters and setters
    public int getFieldWidth() { return fieldWidth; }
    public void setFieldWidth(int fieldWidth) { this.fieldWidth = fieldWidth; }

    public int getFieldHeight() { return fieldHeight; }
    public void setFieldHeight(int fieldHeight) { this.fieldHeight = fieldHeight; }

    public int getStartLevel() { return startLevel; }
    public void setStartLevel(int startLevel) { this.startLevel = startLevel; }

    public boolean isMusic() { return music; }
    public void setMusic(boolean music) { this.music = music; }

    public boolean isSoundEffect() { return soundEffect; }
    public void setSoundEffect(boolean soundEffect) { this.soundEffect = soundEffect; }

    public boolean isExtendedMode() { return !playerTwoType.equals("None");}

    public boolean isPlayerOneType(String type) { return playerOneType.equals(type); }
    public boolean isPlayerTwoType(String type) { return playerTwoType.equals(type); }

    public String getPlayerOneType() { return playerOneType; }
    public void setPlayerOneType(String playerOneType) { this.playerOneType = playerOneType; }

    public String getPlayerTwoType() { return playerTwoType; }
    public void setPlayerTwoType(String playerTwoType) { this.playerTwoType = playerTwoType; }
}