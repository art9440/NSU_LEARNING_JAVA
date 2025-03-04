package org.minesweeper.game;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.consoleView.ConsoleView;

public class Main {
    public static void main(String[] args) {
        GameModel model = new GameModel(args[0]);
        if (args[0].equals("GUI") || args[0].isEmpty()){
            GUIView view = new GUIView(model);
            view.showMainMenu();
        }
        else if (args[0].equals("Console")){
            ConsoleView view = new ConsoleView();
        }

    }
}