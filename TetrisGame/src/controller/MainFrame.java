package controller;

import model.TetrisShape;
import model.gamefactory.*;
import model.TetrisBlock;
import view.panel.GamePanel;
import view.panel.GamePanelMulti;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;

// Singleton pattern for thread safety with lazy initialisation
// MainFrame is the facade class that controls most of the game
public class MainFrame extends MainFrameBase {
    private static MainFrame instance;

    private GamePanel gamePanel;
    private TetrisShape[] nextPieces;

    private PanelsController panels;

    // Holds two threads (One for gameplay, one for rendering)
    private GameController gameLogicOne;
    private Game gameOne;

    // Holds two more threads for extended mode (One for gameLogic, one for rendering)
    private GameController gameLogicTwo; // For extended mode
    private Game gameTwo; // For extended mode

    // Factory design pattern
    private GameFactory gameFactory;

    // Singleton pattern: mainFrame is the only instance of MainFrame. Facade: the main class that controls most of the game
    private MainFrame(String title, int mainWidth, int mainHeight)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        super(title, mainWidth, mainHeight); // Call the superclass constructor
        this.panels = new PanelsController(this);
        this.gameFactory = new GameFactory();
    }

    // Singleton pattern for thread safety with the lazy initialisation
    public static synchronized MainFrame getInstance(String title, int mainWidth, int mainHeight)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (instance == null) {
            instance = new MainFrame(title, mainWidth, mainHeight);
        }
        return instance;
    }

    // Each player draws from the same pool of 1000 prototypal piece shapes (nextPieces)
    public TetrisBlock getNextBlock(int index) {
        int idx = index % 1000; // Wrap around to the beginning of the array
        TetrisBlock block = TetrisBlock.prototype(nextPieces[idx]); // Instantiate an object from the prototype
        block.spawnBlock();
        return block;
    }

    // Makes 1000 blocks and stores them in nextPieces
    public void batchSpawnBlocks() {
        this.nextPieces = new TetrisShape[1000];
        for (int i = 0; i < 1000; i++) {
            TetrisShape shape = TetrisShape.getRandomShape();
            nextPieces[i] = shape;
        }
    }

    //Only one game if not extended mode
    public void initSoloGame() {
        batchSpawnBlocks();
        gamePanel = new GamePanel(this);
        this.gameOne = gameFactory.createGame(this, gamePanel, 1);

        this.gameLogicOne = new GameController(this, gameOne);
        gamePanel.setGame(gameOne, null);
    }

    //Two games if extended mode
    public void initMultiplayerGame() {
        // Second player threads
        batchSpawnBlocks();
        this.gamePanel = new GamePanelMulti(this);

        this.gameOne = gameFactory.createGame(this, gamePanel, 1);
        this.gameLogicOne = new GameController(this, gameOne);

        this.gameTwo = gameFactory.createGame(this, gamePanel, 2);
        this.gameLogicTwo = new GameController(this, gameTwo);

        this.gamePanel.setGame(gameOne, gameTwo);
    }

    public void showGamePanel() { panels.showGamePanel(); }

    public void showSplashScreen() {
        panels.showSplashScreen();
    }

    public void showMainPanel() {
        panels.showMainPanel();
    }

    public void showConfigurePanel() { panels.showConfigurePanel(); }

    public void startGame() {
        gameLogicOne.startGame();
    }
    public void startGameTwo() {
        gameLogicTwo.startGame();
    }

    public void updateGamePeriod() {
        //set period
        //never let period drop below 50
        if (gameOne.getPeriod() < 50) {
            gameLogicOne.setPeriod(50);
        } else {
            gameLogicOne.setPeriod(gameOne.getPeriod());
            gameLogicOne.updateGamePeriod();
        }

    }

    public void updateGamePeriodTwo() {
        //set period
        if(gameTwo.getPeriod() < 50) {
            gameLogicTwo.setPeriod(50);
        } else {
            gameLogicTwo.setPeriod(gameTwo.getPeriod());
            gameLogicTwo.updateGamePeriod();
        }
    }

    public void pauseGame() {
        // gameLogicOne will also pause gameTwo if extended mode is enabled
        gameLogicOne.pauseGame();
        //game logic two pause
        if (!getConfigData().isPlayerTwoType("None")) {
            gameLogicTwo.pauseGame();
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public GameController getGameLogicOne() {
        return gameLogicOne;
    }

    public GameController getGameLogicTwo() {
        return gameLogicTwo;
    }

    // Important for rendering the current board state in the field pane, used by GAME PANEL
    public void repaintBoard() {
        if (gamePanel != null) {
            gamePanel.updateField();
            if (getConfigData().isExtendedMode()) {
                gamePanel.updateFieldTwo(gameTwo.getBoard());
            }
        } else {
            System.err.println("Error: gamePanel is null");
        }
    }

    public void toggleSound() {
        if (getConfigData().isSoundEffect()) {
            setSoundEffect(false);
            gamePanel.updateMessageLabel("Sound Off");
        } else {
            setSoundEffect(true);
            gamePanel.updateMessageLabel("Sound On");
        }
    }


    public void toggleMusic() {
        if (getConfigData().isMusic()) {
            configData.setMusic(false);
            //stop the playing sound
            stopMusic();
            gamePanel.updateMessageLabel("Music Off");
        } else {
            setMusic(true);
            //play the music
            if(!gameOne.isPaused()) {
                playMusic();
            }
            gamePanel.updateMessageLabel("Music On");
        }
    }

    public void gameOverLoser(Game game) {
        //call Game over loser for mainFramGameLogic
        if (game.equals(gameOne)) {
            gameLogicOne.gameOverLoser();
            gameLogicTwo.gameOverWinner();
        } else {
            gameLogicTwo.gameOverLoser();
            gameLogicOne.gameOverWinner();
        }
        gamePanel.gameOver();
    }

    public void resetGames() {
        gameOne.resetGame();
        if (getConfigData().isExtendedMode()) {
            gameTwo.resetGame();
        }
    }
}