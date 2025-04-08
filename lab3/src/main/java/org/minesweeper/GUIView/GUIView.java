package org.minesweeper.GUIView;

import org.minesweeper.GUIView.AboutWindow.About;
import org.minesweeper.GUIView.HighScoresWindow.HighScores;
import org.minesweeper.GUIView.MainMenuWindow.MainMenu;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.GUIView.SettingsWindow.Settings;
import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIView {
    private final GameModel model;
    private final MainMenu mainMenu;
    private final About about;
    private final HighScores highScores;
    private final ButtonsListener buttonsListener;

    public GUIView(GameModel model){
        this.model = model;
        this.mainMenu = new MainMenu("MinesWeeper", 300, 300);
        this.about = new About("About", 400, 600);
        this.highScores = new HighScores("High Scores", 400, 600);
        this.buttonsListener = new ButtonsListener(model, this, mainMenu);

    }


    public void showMainMenu(){

        mainMenu.initWindow(model, this, buttonsListener);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true);
        mainMenu.setMinimumSize(new Dimension(300, 400));
    }

    public void showAbout(){
        about.initWindow(model, this, buttonsListener);
        about.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        about.setResizable(true);
        about.setVisible(true);
        about.setMinimumSize(new Dimension(400, 300));
    }

    public void showHighScores(){
        highScores.initWindow(model, this, buttonsListener);
        highScores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        highScores.setResizable(true);
        highScores.setVisible(true);
        highScores.setMinimumSize(new Dimension(300, 400));
    }

    public void showSettings(){
        Settings settings = new Settings("Settings", 400, 400);
        buttonsListener.setSettingsFrame(settings);
        settings.initWindow(model, this, buttonsListener);
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setResizable(false);
        settings.setVisible(true);
    }

    public void showGame(){
        SwingUtilities.invokeLater(() -> {
            MinesWeeper minesWeeper = new MinesWeeper("Mines Weeper");
            buttonsListener.setMinesWeeperFrame(minesWeeper);
            minesWeeper.setSize(model.getFieldWidth() * 40, model.getFieldHeight() * 40);
            minesWeeper.initWindow(model, this, buttonsListener);
            minesWeeper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            minesWeeper.setResizable(true);
            minesWeeper.setVisible(true);
            minesWeeper.setMinimumSize(new Dimension(300, 400));
        });
    }

    public void setUnvisiableAllFrames(){
        about.setVisible(false);
        highScores.setVisible(false);
    }
}
