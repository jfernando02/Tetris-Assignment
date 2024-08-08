import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static JFrame frame;

    //create main title, background color, frame size
    public static void main(String[] args) {
        frame = new JFrame();
        frame.setSize(350, 520);
        frame.setResizable(false);
        frame.setTitle("Main Screen");
        frame.getContentPane().setBackground(new Color(0x8C8C9C));

        //Creating Buttons displayed on the Main screen, and placement on screen
        JButton playButton = new JButton("Play");
        JButton configButton = new JButton("Configuration");
        JButton highScoresButton = new JButton("HighScores");
        JButton exitButton = new JButton("Exit");

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        configButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.add(Box.createVerticalStrut(40));
        frame.add(playButton);
        frame.add(Box.createVerticalStrut(35));
        frame.add(configButton);
        frame.add(Box.createVerticalStrut(35));
        frame.add(highScoresButton);
        frame.add(Box.createVerticalStrut(35));
        frame.add(exitButton);
        frame.setVisible(true);

        // Adding ActionListener to open ConfigurationWindow
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close Main Screen
                ConfigurationWindow configWindow = new ConfigurationWindow(frame);// Open Configuration Window
            }
        });
    }

    public static void resetMainScreen() {
        frame.setVisible(true); // Reset the main screen to its initial state
    }
}