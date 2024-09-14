package view.panel;

import model.Board;
import model.Game;
import model.Player;
import model.TetrisBlock;
import controller.MainFrame;
import view.field.FieldPane;
import view.field.NextPieceFieldPane;

import javax.swing.*;
import java.awt.*;

public class PlayPanel extends JPanel {
    Game game;
    FieldPane fieldPane;
    NextPieceFieldPane nextPieceFieldPane;
    MainFrame mainFrame;
    Board board;
    int score;
    Player player;
    JLabel messageLabel;

    private JLabel playerLabel;
    private JLabel initialLevelLabel;
    private JLabel levelLabel;
    private JLabel scoreLabel;
    private JLabel linesClearedLabel;

    public PlayPanel(MainFrame mainFrame, Game game) {
        this.mainFrame = mainFrame;
        this.game = game;
        this.board = game.getBoard();
        this.score = game.getScore();
        this.fieldPane = new FieldPane(board, 15);
        this.nextPieceFieldPane = new NextPieceFieldPane(game.getNextPiece(), 15);
        this.player = game.getPlayer();

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

        // Add player info to the left side
        JPanel playerInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.gridx = 0;
        infoGbc.gridy = 0;
        infoGbc.anchor = GridBagConstraints.CENTER;
        infoGbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Center the text in the labels and increase font size
        int size = (mainFrame.getConfigData().getFieldWidth() * 15) / 10;
        Font labelFont = new Font("Arial", Font.BOLD, size);
        playerLabel = new JLabel("Type: " + (player.isAI() ? "AI" : "Human"), JLabel.CENTER);
        playerLabel.setFont(labelFont);
        initialLevelLabel = new JLabel("Initial Level: " + player.getInitialLevel(), JLabel.CENTER);
        initialLevelLabel.setFont(labelFont);
        levelLabel = new JLabel("Current Level: " + player.getLevel(), JLabel.CENTER);
        levelLabel.setFont(labelFont);
        scoreLabel = new JLabel("Current Score: " + player.getScore(), JLabel.CENTER);
        scoreLabel.setFont(labelFont);
        linesClearedLabel = new JLabel("Lines Erased: " + player.getLinesCleared(), JLabel.CENTER);
        linesClearedLabel.setFont(labelFont);

        JLabel nextPieceLabel = new JLabel("Next Piece: ", JLabel.CENTER);
        nextPieceLabel.setFont(labelFont);

        // Render next piece
        nextPieceFieldPane.renderNextPiece(game.getNextPiece());

        // Add components to playerInfoPanel using GridBagLayout
        infoGbc.gridy = 0;
        playerInfoPanel.add(playerLabel, infoGbc);
        infoGbc.gridy++;
        playerInfoPanel.add(initialLevelLabel, infoGbc);
        infoGbc.gridy++;
        playerInfoPanel.add(levelLabel, infoGbc);
        infoGbc.gridy++;
        playerInfoPanel.add(scoreLabel, infoGbc);
        infoGbc.gridy++;
        playerInfoPanel.add(linesClearedLabel, infoGbc);
        infoGbc.gridy++;
        playerInfoPanel.add(nextPieceLabel, infoGbc);
        infoGbc.gridy++;
        playerInfoPanel.add(nextPieceFieldPane, infoGbc);

        // Set preferred size and make the panel transparent
        playerInfoPanel.setPreferredSize(new Dimension(mainFrame.getConfigData().getFieldWidth() * 15, mainFrame.getConfigData().getFieldHeight() * 15));
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
        layeredPane.setPreferredSize(new Dimension(mainFrame.getConfigData().getFieldWidth() * 15, mainFrame.getConfigData().getFieldHeight() * 15));
        fieldPane.setBounds(0, 0, mainFrame.getConfigData().getFieldWidth() * 15, mainFrame.getConfigData().getFieldHeight() * 15);
        fieldPane.setOpaque(true); // Make fieldPane transparent
        layeredPane.add(fieldPane, JLayeredPane.DEFAULT_LAYER);

        // Add message label
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setVerticalAlignment(JLabel.TOP);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setBounds(0, 0, mainFrame.getConfigData().getFieldWidth() * 15, 30); // Adjust y-coordinate to position above the grid
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
        // If message label "Game Paused", don't hide it after 2 seconds
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

    // Update next piece field
    public void updateNextPieceField(TetrisBlock nextPiece) {
        nextPieceFieldPane.renderNextPiece(nextPiece);
        revalidate();
        repaint();
    }

    // Update player info labels
    public void updatePlayerInfo() {
        player = game.getPlayer();
        playerLabel.setText("Type: " + (player.isAI() ? "AI" : "Human"));
        initialLevelLabel.setText("Initial Level: " + player.getInitialLevel());
        levelLabel.setText("Current Level: " + player.getLevel());
        scoreLabel.setText("Current Score: " + player.getScore());
        linesClearedLabel.setText("Lines Erased: " + player.getLinesCleared());
    }
    public void updatePanel() {
        updateNextPieceField(game.getNextPiece());
        updatePlayerInfo();
        // Repaint the panel to update the labels
        revalidate();
        repaint();
    }
}