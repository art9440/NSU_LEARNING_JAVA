package org.minesweeper.controller;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.GUIView.MinesWeeperWindow.FieldButton;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {
    private final GameModel model;
    private final MinesWeeper view;

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
           handleLeftClick(x, y); //раскрытие клетки и соседей
        }
        else if(SwingUtilities.isRightMouseButton(e)){
            handleRightClick(x, y);
        }
    }

    private void handleLeftClick(int x, int y){
        if(model.isFlagged(x, y)) return;

        model.revealCell(x, y);

        if(model.isBomb(x, y)){ //переделать это место, вью не должна ничего знать. Я тыкнул, на модели произошла проверка и модель выкынула проиграл
                                 //ты или нет
            view.updateButton("images/bomb.png", x, y);
            model.stopTimer();
            view.showFailedGameDialog();
        }
        else{
            int count = model.countNearBombs(x, y);
            if (count > 0){
                view.updateButton("images/" + count + ".png", x, y);
            }
            else{
                view.updateButton("images/0.png", x, y);

                model.openCells(x, y, view);
            }
        }

    }



    private void handleRightClick(int x, int y){
        if(model.isFlagged(x, y)){
            view.updateButton("images/none.png", x, y);
            model.changeFlag(x, y);
            model.notRevealed(x, y);
            if(model.isBomb(x, y)){
                model.bombsCountChange(1);
            }
            view.updateBombsCounter(1);
        }
        else if (!model.isRevealed(x, y)){
            view.updateButton("images/flag.png", x, y);
            model.changeFlag(x, y);
            if(model.isBomb(x, y)){
                model.revealCell(x, y);
                model.bombsCountChange(-1);
            }
            view.updateBombsCounter(-1);
            if(model.checkVictory() && view.getBombsRemaining() == 0){
                view.showVictory();
            }
        }
    }

}
