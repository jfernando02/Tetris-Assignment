import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationWindow {

    public ConfigurationWindow(JFrame frame) {
        frame = new JFrame();
        frame.setSize(350, 520);
        frame.setResizable(false);
        frame.setTitle("Configuration Window");
        frame.getContentPane().setBackground(new Color(0x8C8C9C));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel widthPanel = new JPanel();
        widthPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel widthLabel = new JLabel("Field Width:");
        JSlider widthSlider = new JSlider(JSlider.HORIZONTAL, 0, 350, 200);
        widthSlider.setMajorTickSpacing(70);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
        widthPanel.add(widthLabel);
        widthPanel.add(widthSlider);

        JPanel heightPanel = new JPanel();
        heightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel heightLabel = new JLabel("Field Height:");
        JSlider heightSlider = new JSlider(JSlider.HORIZONTAL, 0, 520, 300);
        heightSlider.setMajorTickSpacing(104);
        heightSlider.setPaintTicks(true);
        heightSlider.setPaintLabels(true);
        heightPanel.add(heightLabel);
        heightPanel.add(heightSlider);

        JPanel gameLevelPanel = new JPanel();
        gameLevelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel gameLevelLabel = new JLabel("Game Level:");
        JSlider gameLevelSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
        gameLevelSlider.setMajorTickSpacing(1);
        gameLevelSlider.setPaintTicks(true);
        gameLevelSlider.setPaintLabels(true);
        gameLevelPanel.add(gameLevelLabel);
        gameLevelPanel.add(gameLevelSlider);

        JCheckBox musicCheckBox = new JCheckBox("Music On/Off");

        JCheckBox soundEffectCheckBox = new JCheckBox("Sound Effect On/Off");

        JCheckBox aiPlayCheckBox = new JCheckBox("AI Play");

        JCheckBox extendedModeCheckBox = new JCheckBox("Extended Mode");

        mainPanel.add(widthPanel);

        mainPanel.add(heightPanel);

        mainPanel.add(gameLevelPanel);

        mainPanel.add(gameLevelPanel);

        mainPanel.add(musicCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(soundEffectCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(aiPlayCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(extendedModeCheckBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        frame.add(mainPanel, BorderLayout.CENTER);

        BackButton backButtonPanel = new BackButton(frame); // Create an instance of BackButton
        frame.add(backButtonPanel, BorderLayout.SOUTH);

        // Close the ConfigurationWindow screen and back to the Main Screen
        AbstractButton backButton = new JButton("Back");
        JFrame finalFrame = frame;
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalFrame.dispose();
                Main.resetMainScreen();
            }
        });

        frame.add(backButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}