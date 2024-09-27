package view.panel;

import config.HighScoreData;
import config.HighScoreManager;
import controller.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HighScorePanel extends JPanel {

    private JButton backButton;
    private JLabel[] labels = new JLabel[30];
    private HighScoreManager highScoreManager;

    public HighScorePanel(MainFrame mainFrame, HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
        setLayout(null);

        //background color to black
        setBackground(Color.BLACK);

        createLabels(); // dynamically labels

        // Back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        add(backButton);

        backButton.addActionListener((ActionEvent e) -> {
            mainFrame.playSound("menuKeyPress");
            mainFrame.showMainPanel();
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeComponents();
            }
        });
        resizeComponents();
    }
    private void createLabels() {
        List<HighScoreData> highScoreData = highScoreManager.getTopScores();

        //ranking labels
        for (int i = 0; i < 10; i++) {
            labels[i] = createLabel(String.valueOf(i + 1)); // Rank label (1 to 10)
            add(labels[i]);
        }

        //name labels names
        for (int i = 0; i < 10; i++) {
            if (i < highScoreData.size()) {
                labels[10 + i] = createLabel(highScoreData.get(i).getName());
            } else {
                labels[10 + i] = createLabel("");
            }
            add(labels[10 + i]);
        }

        // labels scores added
        for (int i = 0; i < 10; i++) {
            if (i < highScoreData.size()) {
                labels[20 + i] = createLabel(String.valueOf(highScoreData.get(i).getScore()));
            } else {
                labels[20 + i] = createLabel("0");
            }
            add(labels[20 + i]);
        }
    }

    // JLabel with consistent style
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times Square", Font.PLAIN, 25));
        label.setForeground(Color.WHITE);
        label.setSize(200, 50);
        return label;
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();
        int fontSize = Math.max(20, height / 20);

        for (JLabel label : labels) {
            if (label != null) { // Check for null
                label.setFont(new Font("Times Square", Font.PLAIN, fontSize));
            }
        }

        for (int i = 0; i < 10; i++) {
            int x = 50;
            int y = 50 + i * (fontSize + 20);

            labels[i].setLocation(x, y);
            labels[10 + i].setLocation(x + 100, y);
            labels[20 + i].setLocation(x + 300, y);
        }

        //back button at the bottom
        if (backButton != null) {
            backButton.setBounds((width - 100) / 2, height - 70, 100, 30);
        }
    }
}
