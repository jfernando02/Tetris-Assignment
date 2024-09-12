package config;
// Class to persist player configuration data
public class ConfigData {
    private int fieldWidth;
    private int fieldHeight;
    private int startLevel;
    private boolean music;
    private boolean soundEffect;
    private boolean aiPlay;
    private boolean extendedMode;

    // Constructor with default data
    public ConfigData() {
        this.fieldWidth = 10;
        this.fieldHeight = 20;
        this.startLevel = 1;
        this.music = true;
        this.soundEffect = true;
        this.aiPlay = false;
        this.extendedMode = false;
    }

    // Only getters and setters
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

    public boolean isAiPlay() { return aiPlay; }
    public void setAiPlay(boolean aiPlay) { this.aiPlay = aiPlay; }

    public boolean isExtendedMode() { return extendedMode; }
    public void setExtendedMode(boolean extendedMode) { this.extendedMode = extendedMode; }
}
