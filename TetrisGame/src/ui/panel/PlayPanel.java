package ui.panel;

import model.Board;
import model.Game;
import model.Score;
import ui.MainFrame;
import ui.field.FieldPane;

import javax.swing.*;
import java.awt.*;

public class PlayPanel {
    Game game;
    FieldPane fieldPane;
    MainFrame mainFrame;
    Board board;
    Score score;

    public PlayPanel(MainFrame mainFrame, Game game) {
        this.mainFrame = mainFrame;
        this.game = game;
        this.board = game.getBoard();
        this.score = game.getScore();
        this.fieldPane = new FieldPane(board, 15);

        JPanel playPanel = new JPanel(new GridLayout(1, 2));
        playPanel.setSize(400, 400);
        playPanel.setPreferredSize(new Dimension(400, 400));
        playPanel.setMinimumSize(new Dimension(400, 400));


        // Ensure GamePanel is focusable and requests focus
        playPanel.setFocusable(true);

        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        JPanel rightPanel = new JPanel(new GridLayout(1, 1));

        // Add score label
        JLabel scoreLabel = new JLabel("Score: " + score.getScore());
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setForeground(Color.RED);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leftPanel.add(scoreLabel);

        // Add field pane
        fieldPane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        fieldPane.setPreferredSize(new Dimension(mainFrame.getFieldWidth() * 15, mainFrame.getFieldHeight() * 15));
        rightPanel.add(fieldPane);

        playPanel.add(leftPanel);
        playPanel.add(rightPanel);
    }
}