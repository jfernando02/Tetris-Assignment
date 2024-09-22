package controller;

import model.gamefactory.Game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

public class MainFrameGameLogic {
    private MainFrame mainFrame;
    private ScheduledExecutorService renderExecutor;
    private ScheduledExecutorService gameLogicExecutor;
    private volatile long period;
    private Game game;

    public MainFrameGameLogic(MainFrame mainFrame, Game game) {
        this.mainFrame = mainFrame;
        this.game = game;
    }

    // Starts game threads
    public void startGame() {
        if (game.isGameRunning() && !game.isGameOver()) {
            System.out.println("Game is already running");
            return;
        }
        System.out.println("MainFrame said: New Game Started");
        game.setStartLevel(mainFrame.getConfigData().getStartLevel());
        runGamePeriod();
    }

    // Runs game period
    private void runGamePeriod() {
        if (renderExecutor != null && !renderExecutor.isShutdown()) {
            renderExecutor.shutdownNow();
        }
        if (gameLogicExecutor != null && !gameLogicExecutor.isShutdown()) {
            gameLogicExecutor.shutdownNow();
        }
        renderExecutor = Executors.newSingleThreadScheduledExecutor();
        gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();
        game.start();
        this.period = mainFrame.getGameOne().getPeriod();
        updateGamePeriod();
    }

    // Updates game period until game is over
    public void updateGamePeriod() {
        System.out.println("MainFrame says: Updating period to " + this.period);
        if (!gameLogicExecutor.isShutdown()) {
            gameLogicExecutor.shutdownNow();
        }
        gameLogicExecutor = Executors.newSingleThreadScheduledExecutor();
        renderExecutor.scheduleAtFixedRate(() -> {
            if (game.isPlaying()) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.getGamePanel().repaint();
                });
            }
        }, 0, period / 10, TimeUnit.MILLISECONDS);
        gameLogicExecutor.scheduleAtFixedRate(() -> {
            if (game.isPlaying()) {
                game.play();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    // Also stop second game if extended mode is enabled (only needs to be called once)
    public void stopGame() {
        game.stop();
        // if extended mode is enabled, also stops second game
        System.out.println("MainFrame said: Game Stopped");
    }

    // Also pause second game if extended mode is enabled (only needs to be called once)
    public void pauseGame() {
        game.pause();
        mainFrame.getGamePanel().requestFocusInWindow();
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Game getGame() { return game; }

    public void gameOverLoser() {
        game.gameOverLoser();
    }

    public void gameOverWinner() {
        game.gameOverWinner();
    }
}