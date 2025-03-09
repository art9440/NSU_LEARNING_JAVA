package org.minesweeper.controller;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.GUIView.MinesWeeperWindow.PauseDialog;
import org.minesweeper.GUIView.SettingsWindow.TextFieldProvider;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
            case "Start Game" -> {
                view.dispose();
                GUIView view = new GUIView(model);
                view.showSettings();
            }
            case "Back to menu" -> {
                view.dispose();
                GUIView view = new GUIView(model);
                view.showMainMenu();
            }
            case "Confirm Settings" -> {
                if (view instanceof TextFieldProvider){
                    String[] settings = ((TextFieldProvider) view).getTextField();
                    System.out.println(Arrays.toString(settings));
                    model.setSettings(Integer.parseInt(settings[0]), Integer.parseInt(settings[1]), Integer.parseInt(settings[2]));
                }
                view.dispose();
                model.launchGame();
            }
            case "Default Settings" -> {
                view.dispose();
                model.setSettings(9, 9, 10);
                model.launchGame();
            }
            case "Pause Game" -> {
                if (view instanceof MinesWeeper){
                    ((PauseDialog) view).showPause();
                }
            }
        }
    }
}
