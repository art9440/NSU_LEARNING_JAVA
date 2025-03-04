package org.minesweeper.controller;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.GUIView.HighScores;
import org.minesweeper.GUIView.MainMenu;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsListener implements ActionListener {
    private GameModel model;
    private JFrame view;

    public ButtonsListener(GameModel model, JFrame view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        switch (command) {
            case "Quite" -> model.exitFromApp();
            case "show high scores table" -> {
                view.dispose();
                GUIView view = new GUIView(model);
                view.showHighScores();
            }
            case "Show help" -> {
                view.dispose();
                GUIView view = new GUIView(model);
                view.showAbout();
            }
            case "Start Game" -> model.launchGame();
            case "Back to menu" -> {
                view.dispose();
                GUIView view = new GUIView(model);
                view.showMainMenu();
            }
        }
    }
}
