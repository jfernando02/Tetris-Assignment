import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackButton extends JPanel {

    public BackButton(JFrame frame) {
        JButton backButton = new JButton("Back");
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close config window
                Main.resetMainScreen(); // Back to the Main Screen
            }
        });
    }
}