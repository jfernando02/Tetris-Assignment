package view.panel;

import config.HighScoreData;
import controller.MainFrame;
import view.UIGenerator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {

    private JButton backButton;
    private JButton resetButton;
    private JLabel[] labels = new JLabel[33]; // 30 for scores, 3 for headers
    private MainFrame mainFrame;

    public HighScorePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // Set layout to BorderLayout
        setLayout(new BorderLayout());

        // Set background color for the main panel
        setBackground(new Color(251, 245, 251, 168)); // Light blue background

        // Create a panel for the title and reset button
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false); // Make sure the northPanel is transparent to show the background color

        // Title label
        JLabel titleLabel = new JLabel("Top 10 High Scores", JLabel.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        // Reset button
        resetButton = UIGenerator.createCustomButton("Reset Scores", 150, 20);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-align the reset button
        buttonPanel.setOpaque(false); // Make sure the button panel is transparent
        buttonPanel.add(resetButton);
        northPanel.add(buttonPanel, BorderLayout.WEST); // Add the button panel to the west

        add(northPanel, BorderLayout.NORTH); // Add the north panel to the main layout

        // Score panel to hold ranks, names, and scores
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS)); // Vertical alignment
        scorePanel.setOpaque(false); // Make the score panel transparent
        add(scorePanel, BorderLayout.CENTER);

        createLabels(scorePanel);

        resetButton.addActionListener(e -> {
            mainFrame.playSound("menuKeyPress");
            mainFrame.resetHighScoreData();
            mainFrame.showHighScorePanel();
        });

        backButton = UIGenerator.createCustomButton("Back", 700, 20);
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            mainFrame.playSound("menuKeyPress");
            mainFrame.showMainPanel();
        });
    }

    // Dynamically create the labels for headers, ranks, names, and scores
    private void createLabels(JPanel scorePanel) {
        // Header panel for the three columns (Ranking, Name, Score)
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setOpaque(false); // Make the header panel transparent
        labels[0] = createLabel("Ranking", JLabel.CENTER);
        labels[1] = createLabel("Name", JLabel.LEFT);
        labels[2] = createLabel("Score", JLabel.LEFT);
        headerPanel.add(labels[0]);
        headerPanel.add(labels[1]);
        headerPanel.add(labels[2]);
        scorePanel.add(headerPanel); // Add the header to the score panel

        List<HighScoreData.Score> highScoreData = mainFrame.getHighScoreData().getScores();

        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY); // Slight line (bottom only)

        // For each rank, name, and score, create a new row
        for (int i = 0; i < 10; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 3)); // Create a row panel with 3 columns
            rowPanel.setOpaque(false); // Make the row panel transparent

            // Rank label
            labels[3 + i] = createLabel(String.valueOf(i + 1), JLabel.CENTER);
            rowPanel.add(labels[3 + i]);

            // Name label
            if (i < highScoreData.size()) {
                labels[13 + i] = createLabel(highScoreData.get(i).getName(), JLabel.LEFT);
            } else {
                labels[13 + i] = createLabel("", JLabel.LEFT);
            }
            rowPanel.add(labels[13 + i]);

            // Score label
            if (i < highScoreData.size()) {
                labels[23 + i] = createLabel(String.valueOf(highScoreData.get(i).getScore()), JLabel.LEFT);
            } else {
                labels[23 + i] = createLabel("0", JLabel.LEFT);
            }
            rowPanel.add(labels[23 + i]);

            // Add the row panel to the main score panel
            scorePanel.add(rowPanel);

            // Add a line under the row, except for the last one
            if (i < 9) {
                rowPanel.setBorder(bottomBorder);
            }
        }
    }

    private JLabel createLabel(String text, int alignment) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(alignment);
        label.setVerticalAlignment(JLabel.CENTER);  // Ensures vertical alignment is centered
        return label;
    }
}
