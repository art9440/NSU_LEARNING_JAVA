package org.minesweeper.GUIView;

import org.minesweeper.GUIView.AboutWindow.About;
import org.minesweeper.GUIView.HighScoresWindow.HighScores;
import org.minesweeper.GUIView.MainMenuWindow.MainMenu;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.GUIView.SettingsWindow.Settings;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.*;

public class GUIView {
    private final GameModel model;

    public GUIView(GameModel model){
        this.model = model;
    }
    public void showMainMenu(){
        MainMenu mainMenu = new MainMenu("MinesWeeper", "../images/icon.png", 300, 300);
        mainMenu.initWindow(model);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true);
        mainMenu.setMinimumSize(new Dimension(300, 400));
    }

    public void showAbout(){
        About about = new About("About", 400, 600);
        about.initWindow(model);
        about.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        about.setResizable(true);
        about.setVisible(true);
        about.setMinimumSize(new Dimension(400, 300));
    }

    public void showHighScores(){
        HighScores highScores = new HighScores("High Scores", 400, 600);
        highScores.initWindow(model);
        highScores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        highScores.setResizable(true);
        highScores.setVisible(true);
        highScores.setMinimumSize(new Dimension(300, 400));
    }

    public void showSettings(){
        Settings settings = new Settings("Settings", 400, 400);
        settings.initWindow(model);
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setResizable(false);
        settings.setVisible(true);
    }

    public void showGame(){
        SwingUtilities.invokeLater(() -> {
            MinesWeeper minesWeeper = new MinesWeeper("Mines Weeper", model.getFieldHeight() * 40, model.getFieldWidth() * 40);
            minesWeeper.initWindow(model);
            minesWeeper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            minesWeeper.setResizable(true);
            minesWeeper.setVisible(true);
            minesWeeper.setMinimumSize(new Dimension(300, 400));
        });
    }
}
