package org.minesweeper.game;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.GUIView.MinesWeeperWindow.MinesWeeper;
import org.minesweeper.consoleView.ConsoleView;
import org.minesweeper.controller.ConsoleListener;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GameModel {
    private final String gameMode;
    private Integer fieldWidth;
    private Integer fieldHeight;
    private Integer bombsAmount;
    public Integer bombsCount;
    private Integer[][] bombs;
    private boolean[][] flags; // где стоят флаги
    private boolean[][] revealed;//правильно раскрытые бомбы
    private int elapsedTime = 0;
    private final Timer timer = new Timer(1000, e -> {
        elapsedTime++;
        notifyTimeListeners();
    });
    private final List<Runnable> timeListeners = new ArrayList<>();
    private Boolean loseGame = false;


    public GameModel(String gameMode){
        this.gameMode = gameMode;
    }


    public Integer getFieldWidth(){
        return fieldWidth;
    }

    public Integer getFieldHeight(){
        return fieldHeight;
    }


    public void setSettings(Integer height, Integer width, Integer bombs){
        this.fieldHeight = height;
        this.fieldWidth = width;
        this.bombsAmount = bombs;
    }

    public void exitFromApp(){
        System.exit(0);
    }

    private int getRandomCoordinateX(){
        return (int) (Math.random() * fieldWidth);
    }

    private int getRandomCoordinateY(){
        return (int) (Math.random() * fieldHeight);
    }


    public boolean isBomb(int x, int y){
        return bombs[x][y] == 1;
    }

    public void bombsCountChange(int change){
        bombsCount += change;
    }

    public Integer getBombsCount(){
        return bombsCount;
    }

    public void revealCell(int x, int y){
        revealed[x][y] = true;
        if (isBomb(x, y)){
            loseGame = true;
        }
    }

    public Boolean stateGame(){
        return loseGame;
    }


    public boolean isFlagged(int x, int y){
        return flags[x][y];
    }

    public void changeFlag(int x, int y){
        flags[x][y] = !flags[x][y];
    }

    public boolean isRevealed(int x, int y){
        return revealed[x][y];
    }

    public void notRevealed(int x, int y){
        revealed[x][y] = false;
        if(isBomb(x, y)){
            bombsCountChange(1);
        }
    }

    public void checkBomb(int x, int y){
        if(isBomb(x, y)){
            bombsCountChange(-1);
        }
    }


    public void plantBombs(int _x, int _y){
        int placedBombs = 0;
        while (placedBombs < bombsAmount) {
            int x = getRandomCoordinateX();
            int y = getRandomCoordinateY();

            if ((bombs[y][x] == null || bombs[y][x] == 0) && x != _x && y != _y) {
                bombs[y][x] = 1;
                placedBombs++;
            }
        }
        for (int i = 0; i < fieldHeight; i++){
            System.out.println(Arrays.toString(bombs[i]));
        }
        System.out.println("____________________________________\n");
        startTimer();
    }


    public void launchGame(){
        System.out.println("Launching Game");
        loseGame = false;
        bombs = new Integer[fieldHeight][fieldWidth];
        bombsCount = bombsAmount;
        for (int i = 0; i < fieldHeight; i++){
            Arrays.fill(bombs[i], 0);
        }
        flags = new boolean[fieldHeight][fieldWidth];
        revealed = new boolean[fieldHeight][fieldWidth];
    }

    public int countNearBombs(int x, int y){
        int count = 0;
        for (int dx = -1; dx <= 1; dx ++){
            for (int dy = -1; dy <= 1; dy ++){
                int nx = x + dx, ny = y + dy;
                if(nx >= 0 && ny >= 0 && nx < fieldHeight && ny < fieldWidth){
                    if (bombs[nx][ny] == 1){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean checkVictory(){
        if (bombsCount == 0)
        {
            stopTimer();
            return bombsCount == 0;
        }
        return false;
    }

    private void notifyTimeListeners() {
        for (Runnable listener : timeListeners) {
            listener.run();
        }
    }

    public void startTimer() {
        elapsedTime = 0;
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void resumeTimer() {
        timer.start();
    }

    public int getElapsedTime() {
        return elapsedTime;
    }


    public void addTimeListener(Runnable listener) {
        timeListeners.add(listener);
    }

    public void addToHighScores(String name, Integer time) {
        if(bombsCount == 0) {
            Path path = Paths.get("highScores.csv");
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.newLine();
                writer.write(name + "," + time + "," + this.fieldHeight + "," + this.fieldWidth);
            } catch (IOException e) {
                System.err.println("Error when writing to CSV: " + e.getMessage());
            }
            exitFromApp();
        }else{
            System.err.println("Error: invalid enter in highScores table");
        }
    }


    public void openCells(int x, int y, GameViewInterface view){

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;


                if(nx >= 0 && ny >= 0 && nx < this.getFieldHeight() && ny < this.getFieldWidth()) {

                    if (this.isRevealed(nx, ny) || this.isFlagged(nx, ny)) continue;

                    if (this.isBomb(nx, ny)) continue;

                    this.revealCell(nx, ny);
                    int bombCount = this.countNearBombs(nx, ny);


                    if (bombCount > 0) {
                        if (Objects.equals(gameMode, "GUI")) {
                            view.updateCell(nx, ny, "images/" + bombCount + ".png");
                        }
                        else{
                            view.updateCell(nx, ny, Integer.toString(bombCount));
                        }
                    } else {
                        if (Objects.equals(gameMode, "GUI")){
                            view.updateCell(nx, ny, "images/0.png");
                        }
                        else{
                            view.updateCell(nx, ny, "0");
                        }
                        openCells(nx, ny, view);
                    }
                }
            }
        }
    }
}
