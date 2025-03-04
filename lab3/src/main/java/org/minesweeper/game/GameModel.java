package org.minesweeper.game;

import org.minesweeper.consoleView.ConsoleView;
import org.minesweeper.GUIView.GUIView;

import java.util.HashMap;
import java.util.Map;

public class GameModel {
    private final String gameMode;
    private Map<String, Integer> highScores = new HashMap<>();
    public GameModel(String gameMode){
        this.gameMode = gameMode;
    }

    public void exitFromApp(){
        System.exit(0);
    }


    public void launchGame(){

    }
}
