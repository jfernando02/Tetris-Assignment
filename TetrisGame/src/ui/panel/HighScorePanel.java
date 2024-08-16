package ui.panel;

import ui.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class HighScorePanel extends JPanel {

    private Image image;
    private JButton backButton;
    private JLabel[] labels = new JLabel[30]; // Array to hold all labels

    public HighScorePanel(MainFrame mainFrame) {
        // Load the image using a relative path or as a resource
        URL imageUrl = getClass().getResource("/resources.images/highscore.jpg"); // Adjust path as needed
        if (imageUrl == null) {
            System.err.println("Image not found: highscore.jpg");
        } else {
            image = new ImageIcon(imageUrl).getImage();
        }

        setLayout(null);

        // Create and add labels
        createLabels();

        // Create the "Back" button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel(); // Call method to show MainPanel
            }
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeComponents();
            }
        });

        // Initial resize to set correct positions
        resizeComponents();
    }

    private void createLabels() {
        String[] rankings = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] names = {"STEFAN", "LARRY", "FISTON", "LUCAS", "JACK", "JOSEPH", "DANIEL", "AAA", "AAA", "AAA"};
        String[] scores = {"100000", "95000", "87000", "83000", "72000", "60000", "28000", "000", "000", "000"};

        // Create and add ranking labels
        for (int i = 0; i < 10; i++) {
            labels[i] = createLabel(rankings[i]);
            add(labels[i]);
        }

        // Create and add name labels
        for (int i = 0; i < 10; i++) {
            labels[10 + i] = createLabel(names[i]);
            add(labels[10 + i]);
        }

        // Create and add score labels
        for (int i = 0; i < 10; i++) {
            labels[20 + i] = createLabel(scores[i]);
            add(labels[20 + i]);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Bauhaus 93", Font.BOLD, 30)); // Default size, will be updated in resizeComponents
        label.setForeground(Color.WHITE);
        label.setSize(200, 50); // Use size instead of bounds for scaling
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        // Calculate font size as a percentage of panel height
        int fontSize = Math.max(20, height / 20); // Adjust percentage as needed

        // Update font size for all labels
        for (JLabel label : labels) {
            label.setFont(new Font("Bauhaus 93", Font.BOLD, fontSize));
        }

        // Define relative positions as percentages
        int[] xPositions = {10, 25, 40, 55, 70}; // X positions for ranking
        int[] yPositions = {15, 35}; // Y positions for different rows

        // Update label positions
        for (int i = 0; i < 10; i++) {
            int x = (int) (xPositions[i % 5] / 85.0 * width);
            int y = (i < 5) ? (int) (yPositions[0] / 150.0 * height) : (int) (yPositions[1] / 60.0 * height);
            labels[i].setLocation(x, y);
            labels[10 + i].setLocation(x, y + fontSize + 10); // Adjust position for names
            labels[20 + i].setLocation(x, y + 2 * (fontSize + 10)); // Adjust position for scores
        }

        // Center the button horizontally at the bottom
        if (backButton != null) {
            backButton.setBounds((width - 100) / 2, height - 70, 100, 30);
        }
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame("Tetris High Score", 800, 600);
        HighScorePanel highScorePanel = new HighScorePanel(frame);

        frame.add(highScorePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set a default size
        frame.setVisible(true);
    }
}
