package org.minesweeper.game;

import org.minesweeper.consoleView.ConsoleView;
import org.minesweeper.GUIView.GUIView;

import java.util.HashMap;
import java.util.Map;

public class GameModel {
    private final String gameMode;
    private Integer fieldWidth;
    private Integer fieldHeight;
    private Integer bombsAmount;


    public GameModel(String gameMode){
        this.gameMode = gameMode;
    }

    public void setSettings(Integer height, Integer width, Integer bombs){
        this.fieldHeight = height;
        this.fieldWidth = width;
        this.bombsAmount = bombs;
    }

    public void exitFromApp(){
        System.exit(0);
    }


    public void launchGame(){

    }
}
