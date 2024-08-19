// GamePanel which extends JPanel and implements ActionListeners
package ui.panel;

import model.Board;
import model.Game;
import model.TetrisCell;
import ui.MainFrame;
import ui.field.FieldPane;

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

        fieldPane = new FieldPane(game.getBoard(), 15);
        fieldPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        fieldPane.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15,
                mainFrame.getFieldHeight() * 15));

        centerPanel.add(fieldPane, gbc);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        pauseButton = new JButton("Pause");

        startButton.addActionListener(e -> {
            startGame();
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
            mainFrame.showMainPanel();
            game.setPlaying(false);
        });
        add(backButton, BorderLayout.SOUTH);

        // Add KeyListener to GamePanel
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //convert key code to string
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                System.out.println("Key pressed: " + keyText);
                game.update(e.getKeyCode());
            }
        });

        // Add level label
        levelLabel = new JLabel("Level: " + mainFrame.getLevel());
        levelLabel.setHorizontalAlignment(JLabel.LEFT);
        add(levelLabel, BorderLayout.WEST);
    }

    // to request focus after GamePanel has been added to the window
    /* TODO: testing if we need this or not
    @Override
    public void addNotify() {
        super.addNotify();
        // Request focus after GamePanel has been added to the window
        requestFocusInWindow();
    }
    */

    // For controller to update the field
    private void startGame() {
        mainFrame.startGame();
        System.out.println("GamePanel says:Game started");
        requestFocusInWindow(); // Don't delete
    }

    private void stopGame() {
        mainFrame.stopGame();
        System.out.println("GamePanel says: Game stopped");
    }

    private void pauseGame() {
        mainFrame.pauseGame();
        System.out.println("GamePanel says: Game paused");
    }

    // To update the field after changes occur
    public void updateField(Board<TetrisCell> board) {
        fieldPane.updateBoard(board); //don't delete IMPORTANT
    }

    // Method to update the level label
    public void updateLevelLabel() {
        levelLabel.setText("Level: " + mainFrame.getLevel());
    }
}