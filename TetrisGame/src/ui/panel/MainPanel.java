// Main panel with menus and options to other parts of the program: think check high-scores, start game, etc.
package ui.panel;

import ui.MainFrame;
import ui.UIGenerator;
import util.soundEffects; // TO INCLUDE SOUND PLAY
import util.backgroundMusic; // TO INCLUDE MUSIC PALY

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel backgroundLabel;
    private JLabel logoLabel;
    private JPanel buttonPanel;
    private Image backGroundImage;
    private Image menuLogo;

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        // Loads the original background image
        backGroundImage = Toolkit.getDefaultToolkit().getImage("src/resources.images/background2.jpg");
        // Load custom logo image
        menuLogo = new ImageIcon("src/resources.images/MenuLogo.PNG").getImage();

        // Background image label to hold background image
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Create a JLabel to hold the logo image
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setVerticalAlignment(JLabel.TOP);

        // Panel for buttons
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make the panel transparent

        // Constraints of the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // << Add vertical spacing between components here
        gbc.anchor = GridBagConstraints.CENTER;

        // Add the logo to the button panel
        buttonPanel.add(logoLabel, gbc);

        // Main menu buttons
        UIGenerator.CustomButton playButton = UIGenerator.createCustomButton("Play", 0, 0); // 0, 0 means default
        UIGenerator.CustomButton configButton = UIGenerator.createCustomButton("Configuration", 0, 0);
        UIGenerator.CustomButton highScoresButton = UIGenerator.createCustomButton("HighScores", 0, 0);
        UIGenerator.CustomButton exitButton = UIGenerator.createCustomButton("Exit", 0, 0);

        gbc.gridy = 1;
        buttonPanel.add(playButton, gbc);
        gbc.gridy = 2;
        buttonPanel.add(configButton, gbc);
        gbc.gridy = 3;
        buttonPanel.add(highScoresButton, gbc);
        gbc.gridy = 4;
        buttonPanel.add(exitButton, gbc);

        buttonPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        // Adding ActionListener to open the ConfigurePanel
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundEffects.playSound("TetrisGame/src/resources.sounds/MenuKeyPresses.wav"); // TO INCLUDE SOUND PLAY
                mainFrame.showConfigurePanel();
            }
        });

        // Adding ActionListener to Exit the application
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add a component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });

        // Initial resize to set the correct dimensions
        resizeComponents();

        backgroundMusic audioPlayer = new backgroundMusic();
        audioPlayer.playMusic("TetrisGame/src/resources.sounds/InGameMusic.wav");

        // Stop the audio when needed
        // audioPlayer.stopAudio();
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        // Ensure width and height are non-zero
        if (width == 0 || height == 0) {
            return;
        }

        // Scales the background image
        Image scaledImage = backGroundImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledImage));
        backgroundLabel.setBounds(0, 0, width, height);

        // Centers the button panel and adjust button sizes
        buttonPanel.setBounds(0, 0, width, height);
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof UIGenerator.CustomButton) {
                ((UIGenerator.CustomButton) comp).setPreferredSize(new Dimension(width / 4, height / 12));
            }
        }

        // Adjusts the logo size, change width / x and height / y to adjust the size of the logo
        Image scaledLogoImage = menuLogo.getScaledInstance(width, height / 3, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogoImage));
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
}