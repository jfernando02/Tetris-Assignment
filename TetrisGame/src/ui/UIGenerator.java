// HELPER class to generate the UI components for the MainFrame, uses the PANELS and other UI components
// to create different state interfaces/frames for the game
/*
* A helper class (also known as a utility class) is a class that provides methods or functionalities to
* assist other classes. These classes typically contain static methods and are not meant to be instantiated.
* They encapsulate common operations or reusable code that can be used across different parts of an application.
* */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIGenerator {

    public static CustomButton createCustomButton(String text, int width, int height) {
        return new CustomButton(text, width, height);
    }

    public static class CustomButton extends JButton {
        private Color hoverBackgroundColor;
        private Color originalBackgroundColor;

        public CustomButton(String text, int width, int height) {

            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            //if width and height not provided, make 180 and 30 default
            if (width == 0) {
                width = 180;
            }
            if (height == 0) {
                height = 30;
            }
            setPreferredSize(new Dimension(width, height)); // Set preferred size
            setFont(new Font("Arial", Font.BOLD, 12)); // Set font style
            originalBackgroundColor = Color.WHITE;
            hoverBackgroundColor = new Color(220, 220, 220);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverBackgroundColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(originalBackgroundColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground().equals(hoverBackgroundColor) ? hoverBackgroundColor : originalBackgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Rounded corners
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //set border colour manually
            g2.setColor(new Color(72, 113, 170, 255));
            g2.setStroke(new BasicStroke(3)); // Set the border thickness
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); // Draw rounded corners
            g2.dispose();

        }
    }
}
