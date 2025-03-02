package org.minesweeper.game;

import org.minesweeper.consoleView.ConsoleView;
import org.minesweeper.GUIView.GUIView;

public class GameModel {
    private final String gameMode;

    public GameModel(String gameMode){
        this.gameMode = gameMode;
    }

    public void launchApp(){
        if (gameMode.equals("GUI")){
            GUIView view = new GUIView();
            view.showMainMenu();
        }
        else if (gameMode.equals("Console")){
            ConsoleView view = new ConsoleView();

        }
        else{
            GUIView view = new GUIView();
            view.showMainMenu();
        }

    }
}
