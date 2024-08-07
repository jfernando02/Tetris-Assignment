// SplashScreen is the initial screen shown when the game starts with our names on it
// SplashScreen turns into MainFrame after a few seconds (check activity diagram)
package ui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow
{
    private final int duration;
    private JProgressBar progressBar;

    public SplashScreen(int duration)
    {
        this.duration = duration;
        showSplash();
    }

    public void showSplash()
    {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.WHITE);

        // Set the window's bounds
        int width = 450;
        int height = 350;

        // Centering the Window
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Splash Screen - Image
        Image originalImage = Toolkit.getDefaultToolkit().getImage("TetrisGame/src/ui/tetrisSplashScreen.jpg");
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH); // To Scale Image to Window
        JLabel SplashImage = new JLabel(new ImageIcon(scaledImage));

        // Splash Screen - Text
        JLabel textLabel = new JLabel("Group 4, 2805ICT/3815ICT Assignment: ", JLabel.CENTER);
        textLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        JLabel nameLabel = new JLabel("Fiston Kayeye, Jack Carrall, Lucas Setiady, Stefan Barosan, Joseph Fernando", JLabel.CENTER);
        nameLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        // Splash Screen - Border
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 2));

        // Splash Screen - Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); // Display the progress percentage

        // Building the Splash Screen
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(nameLabel, BorderLayout.SOUTH);
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(textLabel, BorderLayout.CENTER);

        content.add(SplashImage, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        // Display
        setVisible(true);

        new Thread(this::updateProgress).start();
    }

    private void updateProgress()
    {
        int increment = duration / 100;
        int delay = Math.max(increment, 60); // To Eliminate Graphic Jitter

        for (int i = 0; i <= 100; i++)
        {
            try {
                final int progress = i; // For Lambda Expression requirements
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                Thread.sleep(delay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        // Hide splash screen after progress completes
        setVisible(false);
    }
}