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
    private Integer[][] mines;
    private boolean[][] flags;
    private boolean[][] revealed;


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
        //хранить все данные о нынешней сессии буду в двумерном массиве.
        //Бомбы раскидываются случайно после первого выбора игрока. До этого их нигде нет.
    }
}
