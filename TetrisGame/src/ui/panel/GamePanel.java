package ui.panel;

import model.Board;
import model.Game;
import model.TetrisCell;
import ui.MainFrame;
import ui.field.FieldPane;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private Game game;
    private FieldPane fieldPane;
    private MainFrame mainFrame;
    private JButton startButton;
    private JButton pauseButton;
    private JLabel levelLabel;
    private JLabel messageLabel;
    private Clip gameMusic;

    public GamePanel(MainFrame mainFrame, Game game) {
        this.mainFrame = mainFrame;
        this.game = game;
        setLayout(new BorderLayout());
        setSize(mainFrame.getWidth(), mainFrame.getHeight());
        setPreferredSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setMinimumSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        mainFrame.setTitle("Tetris Window");

        // Ensure GamePanel is focusable and requests focus
        setFocusable(true);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Add field pane
        fieldPane = new FieldPane(game.getBoard(), 15);
        fieldPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        fieldPane.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15));
        gbc.gridy = 1;
        centerPanel.add(fieldPane, gbc);

        // Create a layered pane to hold the fieldPane and messageLabel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15));
        fieldPane.setBounds(0, 0, mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15);
        layeredPane.add(fieldPane, JLayeredPane.DEFAULT_LAYER);

        // Add message label
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setVerticalAlignment(JLabel.TOP);
        //set it slightly higher
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setBounds(0, 0, mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15);
        messageLabel.setVisible(false);
        layeredPane.add(messageLabel, JLayeredPane.PALETTE_LAYER);

        gbc.gridy = 1;
        centerPanel.add(layeredPane, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");

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

        add(buttonPanel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            mainFrame.playSound("src/resources.sounds/MenuKeyPresses.wav", false);
            stopGame();
        });
        add(backButton, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                System.out.println("Key pressed: " + keyText);
                game.update(e.getKeyCode());
            }
        });

        levelLabel = new JLabel("Level: " + mainFrame.getStartLevel());
        levelLabel.setHorizontalAlignment(JLabel.LEFT);
        add(levelLabel, BorderLayout.WEST);
    }

    private void startGame() {
        mainFrame.playSound("src/resources.sounds/MenuKeyPresses.wav", false);
        mainFrame.startGame();
        System.out.println("GamePanel says: Game started");
        requestFocusInWindow();
    }

    private void stopGame() {
        mainFrame.playSound("src/resources.sounds/MenuKeyPresses.wav", false);
        mainFrame.stopGame();
        System.out.println("GamePanel says: Game stopped");
    }

    private void pauseGame() {
        mainFrame.playSound("src/resources.sounds/MenuKeyPresses.wav", false);
        mainFrame.pauseGame();
        System.out.println("GamePanel says: Game paused");
    }

    public void updateField(Board<TetrisCell> board) {
        fieldPane.updateBoard(board);
    }

    public void updateLevelLabel(int level) {
        levelLabel.setText("Level: " + level);
    }

    public void setPaused(boolean paused) {
        updateMessageLabel(paused ? "Game Paused" : "");
    }

    public void updateMessageLabel(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(!message.isEmpty());
        //if message label "Game Paused", don't hide it after 2 seconds
        if (message.equals("Game Paused") || message.equals("Game Over") || message.equals("")) {
            messageLabel.setVisible(true);
        } else {
            if (!message.isEmpty()) {
                Timer timer = new Timer(2000, e -> {
                    messageLabel.setVisible(false);
                    if (game.isPaused()) {
                        messageLabel.setText("Game Paused");
                        messageLabel.setVisible(true);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}