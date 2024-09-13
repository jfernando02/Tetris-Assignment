package ui.panel;

import javax.swing.*;
import java.awt.*;

public class SplashPanel extends JPanel {
    private final int duration;
    private JProgressBar progressBar;
    private Runnable onComplete;

    public SplashPanel(int duration, Runnable onComplete) {
        this.duration = duration;
        this.onComplete = onComplete;
        showSplash();
    }

    public void showSplash() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        int width = 800;
        int height = 600;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setPreferredSize(new Dimension(width, height));

        Image originalImage = Toolkit.getDefaultToolkit().getImage("src/resources/images/tetrisSplashScreen.jpg");
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel splashImage = new JLabel(new ImageIcon(scaledImage));

        JLabel textLabel = new JLabel("Group 4, 2805ICT/3815ICT Assignment: ", JLabel.CENTER);
        textLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        JLabel nameLabel = new JLabel("Fiston Kayeye, Jack Carrall, Lucas Setiady, Stefan Barosan, Joseph Fernando", JLabel.CENTER);
        nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        Color oraRed = new Color(156, 20, 20, 255);
        setBorder(BorderFactory.createLineBorder(oraRed, 2));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(nameLabel, BorderLayout.SOUTH);
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(textLabel, BorderLayout.CENTER);

        add(splashImage, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        new Thread(this::updateProgress).start();
    }

    private void updateProgress() {
        int increment = Math.max(1, duration / 100);
        int delay = duration / 100;

        for (int i = 0; i <= 100; i++) {
            try {
                final int progress = i;
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (onComplete != null) {
            onComplete.run();
        }
    }
}