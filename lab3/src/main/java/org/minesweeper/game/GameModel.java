package org.minesweeper.game;

import org.minesweeper.GUIView.GUIView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModel {
    private final String gameMode;
    private Integer fieldWidth;
    private Integer fieldHeight;
    private Integer bombsAmount;
    public Integer bombsCount;
    private Integer[][] bombs;
    private boolean[][] flags; // где стоят флаги
    private boolean[][] revealed;//правильно раскрытые бомбы
    private Timer timer;
    private int elapsedTime = 0;
    private final List<Runnable> timeListeners = new ArrayList<>();


    public GameModel(String gameMode){
        this.gameMode = gameMode;
    }

    public Integer getFieldWidth(){
        return fieldWidth;
    }

    public Integer getFieldHeight(){
        return fieldHeight;
    }

    public Integer getBombsAmount(){
        return bombsAmount;
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
        if (bombs[x][y] == 1){
            return true;
        }
        return false;
    }

    public void bombsCountChange(int change){
        bombsCount += change;
    }

    public Integer getBombsCount(){
        return bombsCount;
    }

    public void revealCell(int x, int y){
        revealed[x][y] = true;
    }


    public boolean isFlagged(int x, int y){
        if (flags[x][y]){
            return true;
        }

        return false;
    }

    public void changeFlag(int x, int y){
        if (flags[x][y]){
            flags[x][y] = false;
        }
        else{
            flags[x][y] = true;
        }
    }

    public boolean isRevealed(int x, int y){
        return revealed[x][y];
    }

    public void notRevealed(int x, int y){
        revealed[x][y] = false;
    }

    private void plantBombs(){
        int placedBombs = 0;
        while (placedBombs < bombsAmount) {
            int x = getRandomCoordinateX();
            int y = getRandomCoordinateY();

            if (bombs[y][x] == null || bombs[y][x] == 0) {
                bombs[y][x] = 1;
                placedBombs++;
            }
        }
        for (int i = 0; i < fieldHeight; i++){
            System.out.println(Arrays.toString(bombs[i]));
        }
    }

    public boolean checkBomb(int x, int y){
        return bombs[x][y] == 1;
    }

    public void launchGame(){
        bombs = new Integer[fieldHeight][fieldWidth];
        bombsCount = bombsAmount;
        for (int i = 0; i < fieldHeight; i++){
            Arrays.fill(bombs[i], 0);
        }
        flags = new boolean[fieldHeight][fieldWidth];
        revealed = new boolean[fieldHeight][fieldWidth];
        plantBombs();
        if(gameMode.equals("GUI")) {
            GUIView view = new GUIView(this);
            timer = new Timer(1000, e -> {
                elapsedTime++;
                notifyTimeListeners();
            });
            view.showGame();
        }
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
        return bombsCount == 0;
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
        Path path = Paths.get("highScores.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(name + "," + time + "," + this.fieldHeight + "," + this.fieldWidth);

        }catch (IOException e){
            System.err.println("Error when writing to CSV: " + e.getMessage());
        }
    }
}
