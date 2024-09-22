// src/model/games/Game.java
package model.gamefactory;

import model.Board;
import model.Player;
import model.TetrisBlock;
import model.TetrisCell;
import javax.sound.sampled.Clip;
// Factory Product Interface
public interface Game {
    void newGame();
    void start();
    void play();
    void spawn();
    void pause();
    void resumeGame();
    void stop();
    void update(int keyCode);
    Board<TetrisCell> getBoard();
    boolean isPlaying();
    void resetGame();
    int getPeriod();
    void setStartLevel(int level);
    boolean isGameRunning();
    int getScore();
    Clip getPlayingMusic();
    boolean isPaused();
    TetrisBlock getNextPiece();
    TetrisBlock getActiveShape();
    Player getPlayer();
    boolean isGameOver();
    void gameOverWinner();
    void gameOverLoser();
}