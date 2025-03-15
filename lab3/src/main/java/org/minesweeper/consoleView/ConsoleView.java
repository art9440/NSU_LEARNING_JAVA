package org.minesweeper.consoleView;

import org.minesweeper.controller.ConsoleListener;
import org.minesweeper.game.GameModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final GameModel model;
    String[][] field;

    public ConsoleView(GameModel model){
        this.model = model;
    }

    public void showConsoleMode(){
        ConsoleListener listener = new ConsoleListener(model, this);

        while (true){
            System.out.println("Write these commands: New Game, High Scores, About, Exit");
            System.out.println("> ");
            listener.listenCommand();
        }
    }

    public void showAbout(){
        System.out.println("How to Play Minesweeper.\n " +
                "The goal of the game is to clear the minefield without detonating any mines.\n" +
                "Left Click - Reveals a cell. If it's a mine, you lose.\n" +
                "Right Click - Places a flag to mark a suspected mine.\n" +
                "Numbers indicate how many mines are adjacent to the cell.\n" +
                "Clear the field without hitting a mine to win!");
    }

    public void showHighScores(){
        System.out.println("Name Time Height Width");
        Path filePath = Paths.get("highScores.csv");

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error while loading High Scores: " + e.getMessage());
        }
    }

    public void showSettings(ConsoleListener listener){
        System.out.println("Write Settings like : xx:yy:zz or write 'Default' ");
        System.out.println("Where: xx - Height, yy - Width, zz - Amount of bombs");
        listener.listenSettings();
        model.launchGame();
    }

    public void showGame(){
        ConsoleListener listener = new ConsoleListener(model, this);
        field = new String[model.getFieldHeight()][model.getFieldWidth()];
        startField(model.getFieldHeight(), model.getFieldWidth());

        while(true){
            for (int i = 0; i < model.getFieldHeight(); i++) {
                System.out.println(Arrays.toString(field[i]));
            }
            System.out.println("Write yor action like:\n" +
                    "x y action\n " +
                    "Where x - row, y - column, actoin - 'flag' or 'open");
            System.out.println("> ");
            listener.listenAction();
        }
    }

    private void startField(int height, int width){
        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                field[x][y] = "*";
            }
        }
    }

    public void updateCell(int x, int y, String img){
        field[x][y] = img;
        for (int i = 0; i < model.getFieldHeight(); i++) {
            System.out.println(Arrays.toString(field[i]));
        }
    }

    public void showFailure(){
        System.out.println("You have lost! Write 'Restart' or 'Main Menu'");
        ConsoleListener listener = new ConsoleListener(model, this);

        listener.listenFailureAction();
    }

}
