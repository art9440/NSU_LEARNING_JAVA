package org.minesweeper.GUIView.AboutWindow;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class About extends JFrame{
    private BoxLayout boxLayout;

    public About(String winTitle, int w, int h){
        super(winTitle);

        setSize(w, h);
    }

    public void initWindow(GameModel model){
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ActionListener buttonsListener = new ButtonsListener(model, this);

        JButton backToMenu = new JButton("Back");

        backToMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.add(backToMenu);
        backToMenu.setActionCommand("Back to menu");
        backToMenu.addActionListener(buttonsListener);

        add(buttonsPanel, BorderLayout.NORTH);

        String aboutText = "<html><div style='text-align: center; font-size: 12px;'>"
                + "<h2>How to Play Minesweeper</h2>"
                + "<p>The goal of the game is to clear the minefield without detonating any mines.</p>"
                + "<p><b>Left Click</b> - Reveals a cell. If it's a mine, you lose.</p>"
                + "<p><b>Right Click</b> - Places a flag to mark a suspected mine.</p>"
                + "<p>Numbers indicate how many mines are adjacent to the cell.</p>"
                + "<p>Clear the field without hitting a mine to win!</p>"
                + "</div></html>";

        JLabel aboutLabel = new JLabel(aboutText, SwingConstants.CENTER);
        aboutLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // 🔹 Оборачиваем текст в JScrollPane, чтобы можно было прокручивать
        JScrollPane scrollPane = new JScrollPane(aboutLabel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);

    }
}
