package org.minesweeper.GUIView;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame{
    private String iconPath;
    private ImageIcon iconFile;
    private JPanel buttonsPanel;
    private JButton newGame, about, highScores, exit;
    private final int width, height;

    public MainMenu(String winTitle, String path, int w, int h){
        super(winTitle);
        iconPath = path;
        width = w;
        height = h;

        setSize(width, height);

        buttonsPanel = new JPanel();
        newGame = new JButton("New Game");
        about = new JButton("About");
        highScores = new JButton(("High Scores"));
        exit = new JButton("Exit");

        buttonsPanel.add(newGame);
        buttonsPanel.add(about);
        buttonsPanel.add(highScores);
        buttonsPanel.add(exit);

        getContentPane().add(BorderLayout.NORTH, buttonsPanel);

    }
}
