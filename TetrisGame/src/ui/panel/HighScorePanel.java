package ui.panel;

import ui.MainFrame; // Ensure this is the correct import
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class HighScorePanel extends JPanel {

    private Image image;

    public HighScorePanel(MainFrame parentFrame) {
        // Load the image using a relative path or as a resource
        URL imageUrl = getClass().getResource("/resources/images/highscore.jpg");
        if (imageUrl == null) {
            System.err.println("Image not found: highscore.jpg");
        } else {
            image = new ImageIcon(imageUrl).getImage();
        }

        // Set panel layout to null for absolute positioning
        this.setLayout(null);

        // Create the labels and set their properties
        JLabel Rank1 = createLabel("1", 130, 50);
        JLabel Rank2 = createLabel("2", 260, 50);
        JLabel Rank3 = createLabel("3", 390, 50);
        JLabel Rank4 = createLabel("4", 520, 50);
        JLabel Rank5 = createLabel("5", 650, 50);
        JLabel Rank6 = createLabel("6", 130, 350);
        JLabel Rank7 = createLabel("7", 260, 350);
        JLabel Rank8 = createLabel("8", 390, 350);
        JLabel Rank9 = createLabel("9", 520, 350);
        JLabel Rank10 = createLabel("10", 650, 350);

        JLabel Stefan = createLabel("STEFAN", 100, 100);
        JLabel Larry = createLabel("LARRY", 230, 100);
        JLabel Fiston = createLabel("FISTON", 350, 100);
        JLabel Lucas = createLabel("LUCAS", 490, 100);
        JLabel Jack = createLabel("JACK", 620, 100);

        JLabel StefanScore = createLabel("100,000", 85, 150);
        JLabel LS = createLabel("95,000", 220, 150);
        JLabel FS = createLabel("87,000", 350, 150);
        JLabel LucaS = createLabel("83,000", 480, 150);
        JLabel JS = createLabel("72,000", 600, 150);

        // Create the "Back" button
        JButton button = new JButton("Back");
        button.setPreferredSize(new Dimension(100, 30));  // Set button size
        button.setBounds(350, 520, 100, 30);

        // Add ActionListener to the "Back" button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the main menu
                parentFrame.showMainPanel(); // Call method to show MainPanel
            }
        });

        // Add components to the panel
        this.add(Rank1);
        this.add(Rank2);
        this.add(Rank3);
        this.add(Rank4);
        this.add(Rank5);
        this.add(Rank6);
        this.add(Rank7);
        this.add(Rank8);
        this.add(Rank9);
        this.add(Rank10);
        this.add(Stefan);
        this.add(Larry);
        this.add(Fiston);
        this.add(Lucas);
        this.add(Jack);
        this.add(StefanScore);
        this.add(LS);
        this.add(FS);
        this.add(LucaS);
        this.add(JS);
        this.add(button);
    }

    // Helper method to create labels
    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Bauhaus 93", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 200, 50);
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    public static void main(String[] args) {
        // Create a frame to hold the panel
        MainFrame frame = new MainFrame();
        HighScorePanel highScorePanel = new HighScorePanel(frame);

        // Add the high score panel to the frame
        frame.add(highScorePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Set a default size
        frame.setVisible(true);
    }
}
