package org.minesweeper.GUIView;

import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIView {
    private GameModel model;

    public GUIView(GameModel model){
        this.model = model;
    }
    public void showMainMenu(){
        MainMenu mainMenu = new MainMenu("MinesWeeper", "../images/icon.png", 300, 300);
        mainMenu.initWindow(model);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true);
        mainMenu.setMinimumSize(new Dimension(400, 300));
    }

    public void showAbout(){
        About about = new About("About", 400, 600);
        about.initWindow(model);
        about.getContentPane().add(new AboutText());
        about.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        about.setResizable(true);
        about.setVisible(true);
        about.setMinimumSize(new Dimension(400, 300));
    }

    public void showHighScores(){
        HighScores highScores = new HighScores("High Scores", 400, 600);
        highScores.initWindow(model);
        highScores.getContentPane().add(new HighScoresText());
        highScores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        highScores.setResizable(true);
        highScores.setVisible(true);
        highScores.setMinimumSize(new Dimension(400, 300));
    }

    public void showSettings(){
        Settings settings = new Settings("Settings", 400, 400);
        settings.initWindow(model);
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setResizable(false);
        settings.setVisible(true);
    }
}
