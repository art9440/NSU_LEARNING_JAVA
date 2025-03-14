package org.minesweeper.GUIView.MainMenuWindow;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame{


    public MainMenu(String winTitle, String path, int w, int h){
        super(winTitle);

        setSize(w, h);


    }

    public void initWindow(GameModel model){
        JPanel buttonsPanel = new JPanel();
        ActionListener buttonsListener = new ButtonsListener(model, this);
        BoxLayout boxLayout = new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(boxLayout);

        JButton newGame = new JButton("New Game");
        JButton about = new JButton("About");
        JButton highScores = new JButton(("High Scores"));
        JButton exit = new JButton("Exit");

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
