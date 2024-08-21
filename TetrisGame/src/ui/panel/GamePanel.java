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
    private JButton stopButton;
    private JButton pauseButton;
    private JLabel levelLabel;
    private JLabel pauseLabel;
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

        // Add pause label
        pauseLabel = new JLabel("Game Paused");
        pauseLabel.setHorizontalAlignment(JLabel.CENTER);
        pauseLabel.setForeground(Color.RED);
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 20));
        pauseLabel.setVisible(false);
        gbc.gridy = 0;
        centerPanel.add(pauseLabel, gbc);

        // Add field pane
        fieldPane = new FieldPane(game.getBoard(), 15);
        fieldPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        fieldPane.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15));
        gbc.gridy = 1;
        centerPanel.add(fieldPane, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        pauseButton = new JButton("Pause");

        startButton.addActionListener(e -> {
            if (!game.isPlaying()) {
                startGame();
                requestFocusInWindow();
            }
            requestFocusInWindow();
        });
        stopButton.addActionListener(e -> stopGame());
        pauseButton.addActionListener(e -> pauseGame());

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
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

        levelLabel = new JLabel("Level: " + mainFrame.getLevel());
        levelLabel.setHorizontalAlignment(JLabel.LEFT);
        add(levelLabel, BorderLayout.WEST);
    }

    private void startGame() {
        mainFrame.playSound("src/resources.sounds/MenuKeyPresses.wav", false);
        mainFrame.startGame();
        System.out.println("GamePanel says:Game started");
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

    public void updateLevelLabel() {
        levelLabel.setText("Level: " + mainFrame.getLevel());
    }

    public void setPaused(boolean paused) {
        pauseLabel.setVisible(paused);
    }
}