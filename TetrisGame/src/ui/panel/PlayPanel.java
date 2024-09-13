package ui.panel;

import model.Board;
import model.Game;
import model.Player;
import model.Score;
import ui.MainFrame;
import ui.field.FieldPane;

import javax.swing.*;
import java.awt.*;

public class PlayPanel extends JPanel {
    Game game;
    FieldPane fieldPane;
    MainFrame mainFrame;
    Board board;
    Score score;
    Player player;
    JLabel messageLabel;

    public PlayPanel(MainFrame mainFrame, Game game, Player player) {
        this.mainFrame = mainFrame;
        this.game = game;
        this.board = game.getBoard();
        this.score = game.getScore();
        this.fieldPane = new FieldPane(board, 15);
        this.player = player;

        // Set a black border around the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(new Color(214, 255, 206, 226), 10));

        // 1 row and 2 column grid layout, right side for the fieldPane and left for the player info
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(true); // Make centerPanel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Add player info to the left side (TODO: currently these are dummy values)
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

        // Center the text in the labels and increase font size
        int size = (mainFrame.getFieldWidth() * 15) / 10;
        Font labelFont = new Font("Arial", Font.BOLD, size);
        JLabel playerLabel = new JLabel("Player: " + (player.isAI() ? "AI" : "Human"), JLabel.CENTER);
        playerLabel.setFont(labelFont);
        JLabel levelLabel = new JLabel("Level: " + player.getLevel(), JLabel.CENTER);
        levelLabel.setFont(labelFont);
        JLabel scoreLabel = new JLabel("Score: " + player.getScore(), JLabel.CENTER);
        scoreLabel.setFont(labelFont);
        JLabel linesClearedLabel = new JLabel("Lines Cleared: " + player.getLinesCleared(), JLabel.CENTER);
        linesClearedLabel.setFont(labelFont);

        JLabel nextPieceLabel = new JLabel("Next Piece: ", JLabel.CENTER);
        nextPieceLabel.setFont(labelFont);


        playerInfoPanel.add(playerLabel);
        playerInfoPanel.add(levelLabel);
        playerInfoPanel.add(scoreLabel);
        playerInfoPanel.add(linesClearedLabel);
        playerInfoPanel.add(nextPieceLabel);

        // Set preferred size and make the panel transparent
        playerInfoPanel.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15));
        playerInfoPanel.setOpaque(true);

        // Add a blue border around the player info section
        playerInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2), "Player Info", 0, 0, labelFont, Color.BLUE));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0; // Ensure the panel takes up the full vertical space
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(playerInfoPanel, gbc);

        // Create a layered pane to hold the fieldPane and messageLabel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15));
        fieldPane.setBounds(0, 0, mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15);
        fieldPane.setOpaque(true); // Make fieldPane transparent
        layeredPane.add(fieldPane, JLayeredPane.DEFAULT_LAYER);

        // Add message label
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setVerticalAlignment(JLabel.TOP);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setBounds(0, 0, mainFrame.getFieldWidth() * 15, 30); // Adjust y-coordinate to position above the grid
        messageLabel.setVisible(false);
        layeredPane.add(messageLabel, JLayeredPane.PALETTE_LAYER);

        // Add fieldPane to the right side
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        centerPanel.add(layeredPane, gbc);

        add(centerPanel, BorderLayout.CENTER);
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