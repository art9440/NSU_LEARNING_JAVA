package org.minesweeper.controller;

import org.minesweeper.GUIView.GUIView;
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
           handleLeftClick(x, y); //раскрытие клетки и соседей
        }
        else if(SwingUtilities.isRightMouseButton(e)){
            handleRightClick(x, y);
        }
    }

    private void handleLeftClick(int x, int y){
        System.out.println("Size of field" + model.getFieldHeight() + "," + model.getFieldWidth());
        if(model.isFlagged(x, y)) return;

        model.revealCell(x, y);

        if(model.isBomb(x, y)){
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

                openCells(x, y, model);
            }
        }

    }

    private void openCells(int x, int y, GameModel model){
        System.out.println("Открываем клетку: (" + x + ", " + y + ")");

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;

                System.out.println("Проверяем соседнюю клетку: (" + nx + ", " + ny + ")");

                if(nx >= 0 && ny >= 0 && nx < model.getFieldHeight() && ny < model.getFieldWidth()) {

                    if (model.isRevealed(nx, ny) || model.isFlagged(nx, ny)) continue;

                    if (model.isBomb(nx, ny)) continue;

                    model.revealCell(nx, ny);
                    int bombCount = model.countNearBombs(nx, ny);

                    System.out.println("Количество соседних бомб для клетки (" + nx + ", " + ny + "): " + bombCount);


                    if (bombCount > 0) {
                        view.updateButton("images/" + bombCount + ".png", nx, ny);
                    } else {
                        view.updateButton("images/0.png", nx, ny);
                        openCells(nx, ny, model);
                    }
                }
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
            if(model.checkVictory()){
                view.showVictory();
            }
        }
    }

}
