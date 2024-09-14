package controller;


import model.Board;
import model.Game;

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
        if (mainFrame.getGame().isGameRunning() && !mainFrame.getGame().isGameOver()) {
            System.out.println("Game is already running");
            return;
        }
        System.out.println("MainFrame said: New Game Started");
        mainFrame.getGame().setStartLevel(mainFrame.getConfigData().getStartLevel());
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
        mainFrame.getGame().start();
        this.period = mainFrame.getGame().getPeriod();
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
            if (mainFrame.getGame().isPlaying()) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.getGamePanel().repaint();
                });
            }
        }, 0, period / 10, TimeUnit.MILLISECONDS);
        gameLogicExecutor.scheduleAtFixedRate(() -> {
            if (mainFrame.getGame().isPlaying()) {
                mainFrame.getGame().play();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    public void stopGame() {
        mainFrame.getGame().stop();
        System.out.println("MainFrame said: Game Stopped");
    }

    public void pauseGame() {
        mainFrame.getGame().pause();
        mainFrame.getGamePanel().requestFocusInWindow();
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}