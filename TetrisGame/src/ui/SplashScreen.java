// SplashScreen is the initial screen shown when the game starts with our names on it
// SplashScreen turns into MainFrame after a few seconds (check activity diagram)
package ui;

import javax.swing.*;
import java.awt.*;

// Error: Progress Bar doesn't go all the way to 100% (More noticeable on longer durations)
// Will add more information later on (names, etc).

public class SplashScreen extends JWindow
{
    int duration;
    public SplashScreen(int duration)
    {
        this.duration = duration;
    }

    public void showSplash(int duration)
    {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        // Set the window's bounds
        int width = 400;
        int height = 300;

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
        JLabel textLabel = new JLabel("Group 4, 2805ICT/3815ICT Assignment", JLabel.CENTER);
        textLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        // Splash Screen - Border
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 2));

        // Splash Screen - Progress Bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true); // Display the progress percentage

        // Building the Splash Screen
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(progressBar, BorderLayout.SOUTH);
        bottomPanel.add(textLabel, BorderLayout.NORTH);

        content.add(SplashImage, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        // Display
        setVisible(true);

        new Thread(() -> {
            int increment = duration / 100;
            for (int i = 0; i <= 100; i++)
            {
                try
                {
                    progressBar.setValue(i); // Update progress bar value
                    Thread.sleep(increment);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        // Wait a little while, maybe showing a progress bar
        try
        {
            Thread.sleep(duration);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setVisible(false);
    }
    public void showSplashAndExit(int duration)
    {
        showSplash(duration);
        System.exit(0);
    }
}