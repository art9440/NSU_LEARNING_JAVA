package org.minesweeper.GUIView;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class About extends JFrame{
    private JPanel buttonsPanel;
    private BoxLayout boxLayout;
    private JButton backToMenu;
    private final int width, height;
    private ActionListener buttonsListener;

    public About(String winTitle, int w, int h){
        super(winTitle);
        width = w;
        height = h;

        setSize(width, height);
    }

    public void initWindow(GameModel model){
        buttonsPanel = new JPanel();
        buttonsListener = new ButtonsListener(model, this);
        boxLayout = new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(boxLayout);


        backToMenu = new JButton("Back");

        backToMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.add(backToMenu);
        backToMenu.setActionCommand("Back to menu");

        backToMenu.addActionListener(buttonsListener);

        getContentPane().add(BorderLayout.NORTH, buttonsPanel);
    }
}
