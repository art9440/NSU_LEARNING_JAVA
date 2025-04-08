package org.minesweeper.controller;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.GUIView.MainMenuWindow.MainMenu;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.GUIView.SettingsWindow.Settings;
import org.minesweeper.game.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ButtonsListener implements ActionListener {
    private final GameModel model;
    private final GUIView view;
    private final MainMenu mainMenu;
    private MinesWeeper minesWeeper;
    private Settings settings;

    public ButtonsListener(GameModel model, GUIView view, MainMenu mainMenu){
        this.model = model;
        this.view = view;
        this.mainMenu = mainMenu;
    }

    public void setSettingsFrame(Settings settings){
        this.settings = settings;
    }

    public void setMinesWeeperFrame(MinesWeeper minesWeeper){
        this.minesWeeper = minesWeeper;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        switch (command) {
            case "Quite" -> model.exitFromApp();
            case "show high scores table" -> {
                mainMenu.setVisible(false);
                view.showHighScores();
            }
            case "Show help" -> {
                mainMenu.setVisible(false);
                view.showAbout();
            }
            case "Start Game" -> {
                mainMenu.setVisible(false);
                view.showSettings();
            }
            case "Back to menu from Game" -> {
                minesWeeper.dispose();
                minesWeeper = null;
                model.stopTimer();
                view.showMainMenu();
            }
            case "Back to menu from hs and ab" -> {
                view.setUnvisiableAllFrames();
                view.showMainMenu();
            }
            case "Back to menu from Settings" -> {
                settings.dispose();
                settings = null;
                view.showMainMenu();
            }
            case "Confirm Settings" -> {
                String[] settingsParam = settings.getTextField();
                System.out.println(Arrays.toString(settingsParam));
                model.setSettings(Integer.parseInt(settingsParam[0]), Integer.parseInt(settingsParam[1]), Integer.parseInt(settingsParam[2]));
                settings.dispose();
                model.launchGame();
                view.showGame();
            }
            case "Default Settings" -> {
                settings.dispose();
                settings = null;
                model.setSettings(9, 9, 10);
                model.launchGame();
                view.showGame();
            }
            case "Pause Game" -> {
                model.stopTimer();
                minesWeeper.showPause();

            }
            case "Restart Game" -> {
                minesWeeper.dispose();
                minesWeeper = null;
                model.stopTimer();
                model.launchGame();
                view.showGame();
            }
        }
    }
}
