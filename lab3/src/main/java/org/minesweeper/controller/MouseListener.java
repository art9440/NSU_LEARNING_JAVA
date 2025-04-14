package org.minesweeper.controller;

import org.minesweeper.GUIView.MinesWeeperWindow.FieldButton;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {
    private final GameModel model;
    private final MinesWeeper view;
    private boolean firstClick = false;

    public MouseListener(GameModel model, MinesWeeper view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        FieldButton button = (FieldButton) e.getSource();
        int x = button.getXCoord();
        int y = button.getYCoord();
        if (!firstClick){
            model.plantBombs(x , y);
            firstClick = true;
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            handleLeftClick(x, y); //раскрытие клетки и соседей
        } else if (SwingUtilities.isRightMouseButton(e)) {
            handleRightClick(x, y);
        }
    }

    private void handleLeftClick(int x, int y){
        if(model.isFlagged(x, y)) return;

        model.revealCell(x, y);

        if (model.stateGame()){
            view.openAllBombs();
            model.stopTimer();
            view.showFailedGameDialog();
        }
        else{
            int count = model.countNearBombs(x, y);
            if (count > 0){
                view.updateCell(x, y, "images/" + count + ".png");
            }
            else{
                view.updateCell(x, y, "images/0.png");

                model.openCells(x, y, view);
            }
        }

    }



    private void handleRightClick(int x, int y){
        if(model.isFlagged(x, y)){
            view.updateCell(x, y, "images/none.png");
            model.changeFlag(x, y);
            model.notRevealed(x, y);
            view.updateBombsCounter(1);
        }
        else if (!model.isRevealed(x, y)){
            view.updateCell(x, y, "images/flag.png");
            model.changeFlag(x, y);
            model.checkBomb(x, y);
            view.updateBombsCounter(-1);
            if(model.checkVictory() && view.getBombsRemaining() == 0){
                view.showVictory();
            }
        }
    }

}
