// Our entry point to the application which will handle the game loop and other game related logic

import controller.MainFrame;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Lazy initialisation of MainFrame (Thread-Safe Singleton pattern)
            MainFrame mainFrame = null;
            try {
                mainFrame = MainFrame.getInstance("Tetris Game", 800, 600);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mainFrame.showSplashScreen();
            mainFrame.setVisible(true);
        });
    }
}