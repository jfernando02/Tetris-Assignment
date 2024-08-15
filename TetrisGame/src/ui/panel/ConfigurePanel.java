// Panel for the configuration screen
// TODO (Idea): hover effect over the settings to explain what they do
package ui.panel;

import ui.UIGenerator;
import ui.MainFrame;
import util.soundEffects; // TO INCLUDE SOUND PLAY

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.awt.*;

public class ConfigurePanel extends JDialog {
    private JSlider widthSlider;
    private JSlider heightSlider;

    // Constructor for the configuration panel
    public ConfigurePanel(MainFrame mainFrame) {
        super(mainFrame, "Configuration", true);
        setLayout(new BorderLayout());
        setSize(mainFrame.getWidth(), mainFrame.getHeight());
        setPreferredSize(new Dimension(mainFrame.getWidth(), mainFrame.getHeight()));
        setResizable(false); // locks the size of the panel, remove line for resizable
        setLocationRelativeTo(mainFrame);
        // make the JDialog immovable
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        // Set background image using JLabel as the content pane
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources.images/configBackground.png"));
        setContentPane(backgroundLabel);
        backgroundLabel.setLayout(new BorderLayout());
        Color whiteBackground = Color.WHITE; // Define white background color
        //getContentPane().setBackground(whiteBackground); // Set background for the entire dialog

        // Title label
        JLabel titleLabel = new JLabel("Configuration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // White box panel for configuration settings
        JPanel configBox = new JPanel(new GridBagLayout());
        configBox.setBackground(whiteBackground);
        configBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        configBox.setPreferredSize(new Dimension(600, 400)); // Size of the white box

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Field Width
        addSlider(configBox, gbc, 0, 0, "Field Width (No of cells):",
                5, 15, mainFrame.getFieldWidth(), e -> {
            int newValue = ((JSlider) e.getSource()).getValue();
            mainFrame.setFieldWidth(newValue);
        });

        // Field Height
        addSlider(configBox, gbc, 0, 1, "Field Height (No of cells):",
                15, 30, mainFrame.getFieldHeight(), e -> {
            int newValue = ((JSlider) e.getSource()).getValue();
            mainFrame.setFieldHeight(newValue);
        });

        // Game Level
        addSlider(configBox, gbc, 0, 2, "Game Level:",
                1, 10, mainFrame.getLevel(), e -> {
            int newValue = ((JSlider) e.getSource()).getValue();
            mainFrame.setLevel(newValue);
        });

        // Music Checkbox
        addCheckBox(configBox, gbc, 0, 3, "Music:",
                mainFrame.isMusic(), e -> mainFrame.setMusic(((JCheckBox) e.getSource()).isSelected()));

        // Sound Effect Checkbox
        addCheckBox(configBox, gbc, 0, 4, "Sound Effect:",
                mainFrame.isSoundEffect(), e -> mainFrame.setSoundEffect(((JCheckBox) e.getSource()).isSelected()));

        // AI Play Checkbox
        addCheckBox(configBox, gbc, 0, 5, "AI Play:",
                mainFrame.isAiPlay(), e -> mainFrame.setAiPlay(((JCheckBox) e.getSource()).isSelected()));

        // Extended Mode Checkbox
        addCheckBox(configBox, gbc, 0, 6, "Extended Mode:",
                mainFrame.isExtendedMode(), e -> mainFrame.setExtendedMode(((JCheckBox) e.getSource()).isSelected()));

        // Back Button
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton backButton = UIGenerator.createCustomButton("Back", 700, 20);
        //centre the button
        //back closes the dialog
        backButton.addActionListener(e -> {
            soundEffects.playSound("TetrisGame/src/resources.sounds/MenuKeyPresses.wav"); // TO INCLUDE SOUND PLAY
            mainFrame.dispose(); // TO INCLUDE SOUND PLAY
        });
        configBox.add(backButton, gbc);

        add(configBox, BorderLayout.CENTER);
    }

    //helper method to create checkboxes
    public static void addSlider(JPanel panel, GridBagConstraints gbc,
                                 int gridx, int gridy, String labelText, int min,
                                 int max, int value, ChangeListener changeListener) {

        gbc.gridx = gridx;
        gbc.gridy = gridy;
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        panel.add(label, gbc);

        gbc.gridx = gridx + 1;
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(Color.WHITE);
        slider.addChangeListener(changeListener);
        panel.add(slider, gbc);

        gbc.gridx = gridx + 2;
        JLabel valueLabel = new JLabel(Integer.toString(value));
        valueLabel.setBackground(Color.WHITE);
        valueLabel.setOpaque(true);
        slider.addChangeListener(e -> valueLabel.setText(Integer.toString(slider.getValue())));
        panel.add(valueLabel, gbc);
    }

    // Helper method to create checkboxes
    public static void addCheckBox(JPanel panel, GridBagConstraints gbc, int gridx,
                                   int gridy, String labelText, boolean isSelected,
                                   ActionListener actionListener) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(Color.WHITE);
        panel.add(label, gbc);

        gbc.gridx = gridx + 1;
        JCheckBox checkBox = new JCheckBox();
        checkBox.addActionListener(actionListener);
        checkBox.setSelected(isSelected);
        checkBox.setBackground(Color.WHITE);
        panel.add(checkBox, gbc);

        gbc.gridx = gridx + 2;
        JLabel valueLabel = new JLabel(isSelected ? "On" : "Off");
        valueLabel.setBackground(Color.WHITE);
        checkBox.addActionListener(e -> valueLabel.setText(checkBox.isSelected() ? "On" : "Off"));
        panel.add(valueLabel, gbc);
    }

}

