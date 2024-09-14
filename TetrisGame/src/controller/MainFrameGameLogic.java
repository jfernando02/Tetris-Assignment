package controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

public class MainFrameGameLogic {
    private MainFrame mainFrame;
    private ScheduledExecutorService renderExecutor;
    private ScheduledExecutorService gameLogicExecutor;
    private volatile long period;

    public MainFrameGameLogic(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // Starts game threads
    public void startGame() {
        if (mainFrame.getGameOne().isGameRunning() && !mainFrame.getGameOne().isGameOver()) {
            System.out.println("Game is already running");
            return;
        }
        System.out.println("MainFrame said: New Game Started");
        mainFrame.getGameOne().setStartLevel(mainFrame.getConfigData().getStartLevel());
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
        mainFrame.getGameOne().start();
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
            if (mainFrame.getGameOne().isPlaying()) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.getGamePanel().repaint();
                });
            }
        }, 0, period / 10, TimeUnit.MILLISECONDS);
        gameLogicExecutor.scheduleAtFixedRate(() -> {
            if (mainFrame.getGameOne().isPlaying()) {
                mainFrame.getGameOne().play();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    // Also stop second game if extended mode is enabled (only needs to be called once)
    public void stopGame() {
        mainFrame.getGameOne().stop();
        // if extended mode is enabled, also stops second game
        System.out.println("MainFrame said: Game Stopped");
    }

    // Also pause second game if extended mode is enabled (only needs to be called once)
    public void pauseGame() {
        mainFrame.getGameOne().pause();
        if (mainFrame.getConfigData().isExtendedMode()) {
            mainFrame.getGameTwo().pause();
        }
        mainFrame.getGamePanel().requestFocusInWindow();
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}