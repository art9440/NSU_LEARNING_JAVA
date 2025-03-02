package org.minesweeper.GUIView;

import javax.swing.*;

public class GUIView {

    public void showMainMenu(){
        MainMenu mainMenu = new MainMenu("MinesWeeper", "../images/icon.png", 300, 300);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true);
    }
}
