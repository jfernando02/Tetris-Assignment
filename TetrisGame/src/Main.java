// Our entry point to the application which will handle the game loop and other game related logic

import controller.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Lazy initialisation of MainFrame (Thread-Safe Singleton pattern)
            MainFrame mainFrame = MainFrame.getInstance("Tetris Game", 800, 600);
            mainFrame.showSplashScreen();
            mainFrame.setVisible(true);
        });
    }
}