package org.minesweeper.controller;

import org.minesweeper.GUIView.MinesWeeperWindow.FieldButton;
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
        FieldButton button = (FieldButton) e.getSource();
        int x = button.getXCoord();
        int y = button.getYCoord();
        if (SwingUtilities.isLeftMouseButton(e)) {
            openCell(button, x, y); //раскрытие клетки и соседей
        }
        else if(SwingUtilities.isRightMouseButton(e)){
            //установка флага
        }
    }

    private void openCell(FieldButton button, int x, int y){
        if(model.checkBomb(x, y)) {

        }
    }
}
