package org.minesweeper.GUIView.MainMenuWindow;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame{
    private String iconPath;
    private ImageIcon iconFile;
    private JPanel buttonsPanel;
    private BoxLayout boxLayout;
    private JButton newGame, about, highScores, exit;
    private final int width, height;
    private ActionListener buttonsListener;


    public MainMenu(String winTitle, String path, int w, int h){
        super(winTitle);
        iconPath = path;
        width = w;
        height = h;

        setSize(width, height);


    }

    public void initWindow(GameModel model){
        buttonsPanel = new JPanel();
        buttonsListener = new ButtonsListener(model, this);
        boxLayout = new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(boxLayout);

        newGame = new JButton("New Game");
        about = new JButton("About");
        highScores = new JButton(("High Scores"));
        exit = new JButton("Exit");

        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        about.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScores.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(newGame);
        buttonsPanel.add(about);
        buttonsPanel.add(highScores);
        buttonsPanel.add(exit);

        newGame.setActionCommand("Start Game");
        about.setActionCommand("Show help");
        highScores.setActionCommand("show high scores table");
        exit.setActionCommand("Quite");

        newGame.addActionListener(buttonsListener);
        about.addActionListener(buttonsListener);
        highScores.addActionListener(buttonsListener);
        exit.addActionListener(buttonsListener);


        getContentPane().add(BorderLayout.NORTH, buttonsPanel);

    }

}
