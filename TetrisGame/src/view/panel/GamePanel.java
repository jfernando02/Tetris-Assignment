package view.panel;

import model.*;
import controller.*;
import model.gamefactory.Game;
import view.UIGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    protected Game game;
    protected MainFrame mainFrame;
    protected JButton startButton;
    protected JButton pauseButton;
    protected PlayPanel playPanel;
    protected boolean wasPaused;
    private volatile int periodOne;
    private volatile int periodTwo;

    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setGame(Game gameOne, Game gameTwo) {
        this.game = gameOne;
        playPanel = new PlayPanel(mainFrame);
        playPanel.setGame(gameOne);

        setLayout(new BorderLayout());
        setSize(mainFrame.getWidth(), mainFrame.getHeight());
        setPreferredSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setMinimumSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        mainFrame.setTitle("Tetris Window");

        // Ensure GamePanel is focusable and requests focus
        setFocusable(true);

        // Set a background image
        JLabel background = new JLabel(new ImageIcon("src/resources/images/tetrisPlayBackground.jpg"));
        //resize according to mainframe
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

        // Create and add PlayPanel to the center
        playPanel.setOpaque(false); // Make PlayPanel transparent
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Make centerPanel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(playPanel, gbc);
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
            stopGame();
        });
        background.add(backButton, BorderLayout.SOUTH);

        //Listens for pause/stop
        keyListenerOptions();

        //Listens for player/AI input
        keyListenerMove();
    }

    //Thread for listening to pause/stop
    public void keyListenerOptions() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //if not game over
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                System.out.println("Key pressed: " + keyText);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P:
                        System.out.println("P key pressed");
                        //if paused, set GamePanel pause button to resume
                        pauseGame();
                        break;
                    case KeyEvent.VK_S:
                        System.out.println("S key pressed"); //stop
                        //toggle sound effect off
                        mainFrame.toggleSound();
                        break;
                    case KeyEvent.VK_M:
                        System.out.println("M key pressed"); //mute
                        //toggle music off
                        mainFrame.toggleMusic();
                        break;
                }
            }
        });
    }

    //Multiplayer panel overrides this method
    public void keyListenerMove() {
        if (!mainFrame.getConfigData().isPlayerOneType("AI")) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    String keyText = KeyEvent.getKeyText(e.getKeyCode());
                    System.out.println("Key pressed: " + keyText);
                    game.update(e.getKeyCode());
                }
            });
        }
    }

    protected void startGame() {
        //clear message label of play panel
        playPanel.messageLabel.setText("");
        mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
        mainFrame.startGame();
        //make Play button say "playing"
        startButton.setText("Playing");
        System.out.println("GamePanel says: Game started");
        requestFocusInWindow();
    }

    public void setStartButtonText(String text) {
        startButton.setText(text);
    }

    public void gameOver() {
        // clear message label
        System.out.println("GamePanel says: Game over");
        playPanel.setGameOverMessage();
    }

    protected void stopGame() {
        mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
        System.out.println("GamePanel says: Game stopped");
        if (game.isPlaying()) {
            game.stop();
        }
        quitDialog();
    }

    // Stop game quit screen
    public void quitDialog() {
        //new JDIalog for game over to ask if they're sure if they want to quit
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Stop Game");
        dialog.setSize(200, 200);
        dialog.setVisible(false);
        //centre the dialog
        dialog.setLocationRelativeTo(null);

        //ask if they want to quit the game
        int result = JOptionPane.showConfirmDialog(dialog,
                "Are you sure you want to quit the game and go to the main menu?", "Stop Game", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.out.println("Game stopped");
            mainFrame.resetGames();
            dialog.dispose();
            //go to main panel
            mainFrame.showMainPanel();
            //reset field pane configuration to default (requirement)
            mainFrame.resetFieldPaneConfig();
        } else {
            mainFrame.pauseGame();
            dialog.dispose();
        }
    }

    public void pauseGame() {
        mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
        mainFrame.pauseGame();
        //if game paused set button text to "Resume"
        if (game.isPaused()) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
        System.out.println("GamePanel says: Game paused");
    }

    public void updateField() {
        playPanel.fieldPane.updateBoard(game.getBoard());
    }

    public void setPaused(boolean paused) {
        playPanel.messageLabel.setText(paused ? "Game Paused" : "");
        playPanel.messageLabel.setVisible(paused);
    }

    public void updateMessageLabel(String soundOff) {
        playPanel.updateMessageLabel(soundOff);
    }

    public void updatePlayPanel() {
        playPanel.updatePanel();
        requestFocusInWindow();
    }

    // Prototype
    public void updateFieldTwo(Board<TetrisCell> board) {}
}