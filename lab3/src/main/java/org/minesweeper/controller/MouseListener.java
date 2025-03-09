package org.minesweeper.controller;

import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {
    private GameModel model;
    private MinesWeeper view;

    public MouseListener(GameModel model, MinesWeeper view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (SwingUtilities.isLeftMouseButton(e)) {

        }
        else if(SwingUtilities.isRightMouseButton(e)){

        }
    }
}
