package view.panel;

import view.UIGenerator;
import controller.MainFrame;

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
        setSize(800, 600);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false); // locks the size of the panel, remove line for resizable
        setLocationRelativeTo(mainFrame);
        // make the JDialog immovable
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        // Set background image using JLabel as the content pane
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/images/configBackground.png"));
        setContentPane(backgroundLabel);
        backgroundLabel.setLayout(new BorderLayout());
        Color whiteBackground = Color.WHITE; // Define white background color

        // Title label
        JLabel titleLabel = new JLabel("Configuration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        backgroundLabel.add(titlePanel, BorderLayout.NORTH);

        // White box panel for configuration settings
        JPanel configBox = new JPanel(new GridBagLayout());
        configBox.setBackground(whiteBackground);
        configBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        configBox.setPreferredSize(new Dimension(600, 400)); // Size of the white box

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            // Reset Config Button
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.EAST;
            //custom button for reset
            JButton resetButton = UIGenerator.createCustomButton("Reset Config", 20, 20);
            resetButton.addActionListener(e -> {
                mainFrame.resetConfigData();
                JOptionPane.showMessageDialog(this, "Configuration has been reset to default.", "Reset Settings", JOptionPane.INFORMATION_MESSAGE);
                // refresh the Configuration Panel
                mainFrame.showConfigurePanel();
                dispose(); // Delete old configuration panel
            });
            configBox.add(resetButton, gbc);

            // Field Width
            addSlider(configBox, gbc, 0, 1, "Field Width (No of cells):",
                    5, 15, mainFrame.getConfigData().getFieldWidth(), e -> {
                        int newValue = ((JSlider) e.getSource()).getValue();
                        mainFrame.getConfigData().setFieldWidth(newValue);
                    });

            // Field Height
            addSlider(configBox, gbc, 0, 2, "Field Height (No of cells):",
                    15, 30, mainFrame.getConfigData().getFieldHeight(), e -> {
                        int newValue = ((JSlider) e.getSource()).getValue();
                        mainFrame.getConfigData().setFieldHeight(newValue);
                    });

            // Game Level
            addSlider(configBox, gbc, 0, 3, "Game Level:",
                    1, 10, mainFrame.getConfigData().getStartLevel(), e -> {
                        int newValue = ((JSlider) e.getSource()).getValue();
                        mainFrame.getConfigData().setStartLevel(newValue);
                    });

            // Music Checkbox
            addCheckBox(configBox, gbc, 0, 4, "Music:",
                    mainFrame.getConfigData().isMusic(), e -> {
                        mainFrame.setMusic(((JCheckBox) e.getSource()).isSelected());
                    });

            // Sound Effect Checkbox
            addCheckBox(configBox, gbc, 0, 5, "Sound Effect:",
                    mainFrame.getConfigData().isSoundEffect(), e -> {
                        mainFrame.setSoundEffect(((JCheckBox) e.getSource()).isSelected());
                    });

            //add combo box For player one and Player two
            String[] playerOneTypes = {"Human", "AI", "External"};
            String[] playerTwoTypes = {"None", "Human", "AI", "External"};

            addComboBox(configBox, gbc, 0, 6, "Player One Type:", playerOneTypes, mainFrame.getConfigData().getPlayerOneType(), e -> {
                String newValue = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
                mainFrame.getConfigData().setPlayerOneType(newValue);
            });

            addComboBox(configBox, gbc, 0, 7, "Player Two Type:", playerTwoTypes, mainFrame.getConfigData().getPlayerTwoType(), e -> {
                String newValue = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
                mainFrame.getConfigData().setPlayerTwoType(newValue);
            });


            // Back Button
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton backButton = UIGenerator.createCustomButton("Back", 700, 20);
            //centre the button
            //back closes the dialog
            backButton.addActionListener(e -> {
                mainFrame.playSound("src/resources/sounds/MenuKeyPresses.wav", false);
                //save the configuration
                mainFrame.saveConfigData();
                dispose();
            });
            configBox.add(backButton, gbc);

            backgroundLabel.add(configBox, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initialising configuration panel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to create sliders
    public static void addSlider(JPanel panel, GridBagConstraints gbc,
                                 int gridx, int gridy, String labelText, int min,
                                 int max, int value, ChangeListener changeListener) {
        if (min > max) {
            throw new IllegalArgumentException("Invalid range properties: min should be less than or equal to max");
        }

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

    // Helper method to create combo boxes
    private void addComboBox(JPanel panel, GridBagConstraints gbc, int gridx, int gridy, String label, String[] options, String selected, ActionListener listener) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel comboBoxLabel = new JLabel(label);
        panel.add(comboBoxLabel, gbc);

        gbc.gridx = gridx + 1;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedItem(selected);
        comboBox.addActionListener(listener);
        panel.add(comboBox, gbc);
    }
}