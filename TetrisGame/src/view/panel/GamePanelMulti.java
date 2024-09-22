package view.panel;

import controller.MainFrame;
import model.gamefactory.Game;
import view.UIGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GamePanelMulti extends GamePanel {
    private Game gameOne;
    private Game gameTwo;
    private PlayPanel playPanelTwo;
    private Set<Integer> pressedKeys = new HashSet<>();

    public GamePanelMulti(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    public void setGame(Game gameOne, Game gameTwo) {
        this.gameOne = gameOne;
        this.gameTwo = gameTwo;

        // Create two playPanels
        playPanel = new PlayPanel(mainFrame);
        playPanel.setGame(gameOne);
        playPanelTwo = new PlayPanel(mainFrame);
        playPanelTwo.setGame(gameTwo);

        setLayout(new BorderLayout());
        setSize(mainFrame.getWidth(), mainFrame.getHeight());
        setPreferredSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setMinimumSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        mainFrame.setTitle("Tetris Window");

        // Ensure GamePanel is focusable and requests focus
        setFocusable(true);
        requestFocusInWindow();

        // Set a background image
        JLabel background = new JLabel(new ImageIcon("src/resources/images/tetrisPlayBackground.jpg"));
        background.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ImageIcon imageIcon = new ImageIcon("src/resources/images/tetrisPlayBackground.jpg");
                Image image = imageIcon.getImage();
                Image newImage = image.getScaledInstance(mainFrame.getWidth(), mainFrame.getHeight(), Image.SCALE_SMOOTH);
                background.setIcon(new ImageIcon(newImage));
            }
        });
        add(background);

        // Create and add PlayPanels to the center
        playPanel.setOpaque(false); // Make PlayPanel transparent
        playPanelTwo.setOpaque(false); // Make PlayPanelTwo transparent
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Make centerPanel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(playPanel, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 20, 0, 0); // Add space between the panels
        centerPanel.add(playPanelTwo, gbc);
        background.setLayout(new BorderLayout());
        background.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make buttonPanel transparent
        startButton = new UIGenerator.CustomButton("Play", 180, 30);
        pauseButton = new UIGenerator.CustomButton("Pause", 180, 30);

        startButton.addActionListener(e -> {
            if (!gameOne.isPlaying()) {
                startGame();
                requestFocusInWindow();
            }
            requestFocusInWindow();
        });

        pauseButton.addActionListener(e -> pauseGame());

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        background.add(buttonPanel, BorderLayout.NORTH);

        JButton backButton = new UIGenerator.CustomButton("Back", 180, 30);
        backButton.setOpaque(false); // Make backButton transparent
        backButton.addActionListener(e -> {
            mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
            mainFrame.stopGame();
        });
        background.add(backButton, BorderLayout.SOUTH);

        keyListenerAI();
        keyListener();

    }
    @Override
    public void gameOver() {
        // game over for both players

    }

    @Override
    public void keyListener() {
        // Add a key listener to track key presses for both games
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());  // Add the pressed key to the set
                updateGames();  // Update both games based on pressed keys
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());  // Remove the released key from the set
                updateGames();  // Update both games again
            }
        });
    }

    public void keyListenerAI() {
        if (mainFrame.getConfigData().isPlayerOneType("AI")) {
            Thread gameOneThread = new Thread(() -> {
                while (true) {
                    if (gameOne.getActiveShape() != null) {
                        gameOne.update(KeyEvent.VK_UP);
                    }
                    try {
                        Thread.sleep(gameOne.getPeriod());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            gameOneThread.start();
        }

        if (mainFrame.getConfigData().isPlayerTwoType("AI")) {
            Thread gameTwoThread = new Thread(() -> {
                while (true) {
                    if (gameTwo.getActiveShape() != null) {
                        gameTwo.update(KeyEvent.VK_UP);
                    }
                    try {
                        Thread.sleep(gameTwo.getPeriod());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            gameTwoThread.start();
        }
    }

    // Method to update both games based on the pressed keys
    private void updateGames() {

        for (int keyCode : pressedKeys) {
            System.out.println("Key pressed: " + KeyEvent.getKeyText(keyCode));
            gameOne.update(keyCode);  // Update gameOne with relevant keys
            gameTwo.update(keyCode);  // Update gameTwo with relevant keys
        }
    }

    @Override
    public void startGame() {
        mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
        mainFrame.startGame();
        mainFrame.startGameTwo();
        // Make Play button say "playing"
        startButton.setText("Playing");
        System.out.println("GamePanel says: Game started");
        requestFocusInWindow();
    }

    @Override
    public void pauseGame() {
        mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
        mainFrame.pauseGame();
        //if game paused set button text to "Resume"
        if (gameOne.isPaused() || gameTwo.isPaused()) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
        System.out.println("GamePanel says: Game paused");
    }

    @Override
    public void updateMessageLabel(String sound) {
        playPanel.setGameOverMessage();
        playPanelTwo.setGameOverMessage();
    }

    @Override
    public void updateField() {
        playPanel.fieldPane.updateBoard(gameOne.getBoard());
        playPanelTwo.fieldPane.updateBoard(gameTwo.getBoard());
    }

    @Override
    public void setPaused(boolean paused) {
        playPanel.messageLabel.setText(paused ? "Game Paused" : "");
        playPanel.messageLabel.setVisible(paused);

        playPanelTwo.messageLabel.setText(paused ? "Game Paused" : "");
        playPanelTwo.messageLabel.setVisible(paused);
    }

    @Override
    public void updatePlayPanel() {
        playPanel.updatePanel();
        playPanelTwo.updatePanel();
        requestFocusInWindow();
    }
}