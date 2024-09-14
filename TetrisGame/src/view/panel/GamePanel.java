package view.panel;

import model.*;
import controller.*;
import view.UIGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private Game game;
    private MainFrame mainFrame;
    private JButton startButton;
    private JButton pauseButton;
    private PlayPanel playPanel;

    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setGame(Game game) {
        this.game = game;
        playPanel = new PlayPanel(mainFrame);
        playPanel.setGame(game);

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
            if (!game.isPlaying()) {
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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                System.out.println("Key pressed: " + keyText);
                game.update(e.getKeyCode());
            }
        });
    }

    private void startGame() {
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

    public void setPauseButtonText(String text) {
        pauseButton.setText(text);
    }

    private void stopGame() {
        mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
        mainFrame.stopGame();
        System.out.println("GamePanel says: Game stopped");
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

    public void updateField(Board<TetrisCell> board) {
        playPanel.fieldPane.updateBoard(board);
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

}