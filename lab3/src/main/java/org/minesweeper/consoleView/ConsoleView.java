package org.minesweeper.consoleView;

import org.minesweeper.controller.ConsoleListener;
import org.minesweeper.game.GameModel;
import org.minesweeper.game.GameViewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class ConsoleView implements GameViewInterface {
    private final GameModel model;
    private String[][] field;
    private int bombsRemaining;

    public ConsoleView(GameModel model){
        this.model = model;
    }

    public int getBombsRemaining(){
        return bombsRemaining;
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
        showGame();
    }

    public void showGame(){
        ConsoleListener listener = new ConsoleListener(model, this);
        field = new String[model.getFieldHeight()][model.getFieldWidth()];
        startField(model.getFieldHeight(), model.getFieldWidth());
        bombsRemaining = model.getBombsCount();

        while(true){
            for (int i = 0; i < model.getFieldHeight(); i++) {
                System.out.println(Arrays.toString(field[i]));
            }
            System.out.println("Write yor action like:\n" +
                    "x y action\n " +
                    "Where x - row, y - column, actoin - 'flag' or 'open");
            System.out.println("Bombs: " + bombsRemaining);
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
    }

    public void updateBombsCounter(int update){
        bombsRemaining += update;
    }

    public void showFailure(){
        System.out.println("You have lost! Write 'Restart' or 'Main Menu'");
        ConsoleListener listener = new ConsoleListener(model, this);

        listener.listenFailureAction();
    }

    public void showVictory(){
        model.stopTimer();

        int time = model.getElapsedTime();

        int minutes = time / 60;
        int seconds = time % 60;

        System.out.println("You have won!");
        System.out.println("Your time: " + minutes + ":" + seconds);
        System.out.println("Write your name: ");
        String name = ConsoleListener.listenName();
        model.addToHighScores(name, time);
        model.exitFromApp();
    }

    public void openAllBombs(){
        for (int x = 0; x < model.getFieldHeight(); x++){
            for(int y = 0; y < model.getFieldWidth(); y++){
                if(model.isBomb(x, y)){
                    updateCell(x, y, "B");
                }
                else{
                    updateCell(x, y, "*");
                }
            }
            System.out.println(Arrays.toString(field[x]));
        }
    }

}
