package view.panel;

import config.HighScoreData;
import config.HighScoreManager;
import controller.MainFrame;
import view.UIGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {

    private JButton backButton;
    private JLabel[] labels = new JLabel[30];
    private HighScoreManager highScoreManager;

    public HighScorePanel(MainFrame mainFrame, HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;

        // layout to BorderLayout
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("High Score", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        // score panel to hold ranks, names, and scores
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(null);
        add(scorePanel, BorderLayout.CENTER);

        createLabels(scorePanel);

        backButton = UIGenerator.createCustomButton("Back", 700, 20);
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            mainFrame.playSound("menuKeyPress");
            mainFrame.showMainPanel();
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeComponents(scorePanel);
            }
        });
        resizeComponents(scorePanel);
    }

    // Dynamically create the labels for ranks, names, and scores
    private void createLabels(JPanel scorePanel) {
        List<HighScoreData> highScoreData = highScoreManager.getTopScores();

        // Rank labels (1 - 10)
        for (int i = 0; i < 10; i++) {
            labels[i] = createLabel(String.valueOf(i + 1));
            scorePanel.add(labels[i]);
        }

        for (int i = 0; i < 10; i++) {
            if (i < highScoreData.size()) {
                labels[10 + i] = createLabel(highScoreData.get(i).getName());
            } else {
                labels[10 + i] = createLabel("");
            }
            scorePanel.add(labels[10 + i]);
        }
        for (int i = 0; i < 10; i++) {
            if (i < highScoreData.size()) {
                labels[20 + i] = createLabel(String.valueOf(highScoreData.get(i).getScore()));
            } else {
                labels[20 + i] = createLabel("0");
            }
            scorePanel.add(labels[20 + i]);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times Square", Font.PLAIN, 50));
        label.setForeground(Color.BLACK);
        label.setSize(150, 50);
        return label;
    }

    private void resizeComponents(JPanel scorePanel) {
        int width = scorePanel.getWidth();
        int height = scorePanel.getHeight();
        int fontSize = Math.max(20, height / 20);
        int rankToNameDistance = 50;
        int nameToScoreDistance = 500;

        for (JLabel label : labels) {
            if (label != null) { // Ensure labels exist
                label.setFont(new Font("Times Square", Font.PLAIN, fontSize));
            }
        }

        for (int i = 0; i < 10; i++) {
            int x = 50;
            int y = 50 + i * (fontSize + 20);

            labels[i].setLocation(x, y);
            labels[10 + i].setLocation(x + rankToNameDistance, y);
            labels[20 + i].setLocation(x + rankToNameDistance + nameToScoreDistance, y);
        }
    }
}
