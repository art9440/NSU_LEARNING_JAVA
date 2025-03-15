package org.minesweeper.controller;

import org.minesweeper.consoleView.ConsoleView;
import org.minesweeper.game.GameModel;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleListener {
    private final GameModel model;
    private final ConsoleView view;
    static Scanner input = new Scanner(System.in);

    public ConsoleListener(GameModel model, ConsoleView view){
        this.model = model;
        this.view = view;
    }

    public void listenCommand(){
        String commandLine = input.nextLine().trim();

        if (commandLine.equals("Exit")){
            model.exitFromApp();
        }
        else if(commandLine.equals("About")){
            view.showAbout();
        } else if (commandLine.equals("High Scores")) {
            view.showHighScores();
        }
        else if(commandLine.equals("New Game")){
            view.showSettings(this);
        }
    }

    public void listenSettings(){
        String commandline = input.nextLine().trim();
        if (commandline.matches("\\d{1,3}:\\d{1,3}:\\d{1,3}")) {
            String[] settings = commandline.split(":");
            int height = Integer.parseInt(settings[0]);
            int width = Integer.parseInt(settings[1]);
            int bombs = Integer.parseInt(settings[2]);
            if (bombs > height * width){
                model.setSettings(9, 9, 10);
            }
            else{
                model.setSettings(height, width, bombs);
            }
        }
        else if(commandline.equals("Default"))
        {
            model.setSettings(9, 9, 10);
        }
        else{
            model.setSettings(9, 9, 10);
        }
    }

    public void listenAction(){
        String commandline = input.nextLine().trim();
        String[] args = commandline.split(" ");
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        String action = args[2];
        System.out.println(x + "," + y + "," + action);
        if (x < model.getFieldHeight() && y < model.getFieldWidth() && x >= 0 && y >= 0) {
            if (action.equals("open")){
                if(model.isFlagged(x, y)){
                    System.out.println("This cell is flagged");
                }
                else if(model.isRevealed(x, y)){
                    System.out.println("This cell is already revealed.");
                }
                else{
                    model.revealCell(x, y);

                    if (model.isBomb(x, y)){
                        view.updateCell(x, y, "B");
                        model.stopTimer();
                        view.showFailure();
                    }
                    else{
                        int count = model.countNearBombs(x, y);
                        if (count > 0){
                            view.updateCell(x, y, Integer.toString(count));
                        }
                        else{
                            view.updateCell(x, y, "0");
                            openCells(x, y, model);
                        }
                    }
                }
            }
            else if (action.equals("flag")){
                if(model.isFlagged(x, y)){
                    view.updateCell(x, y, "*");
                    model.changeFlag(x, y);
                    model.notRevealed(x, y);
                    if (model.isBomb(x, y)){
                        model.bombsCountChange(1);
                    }
                    view.updateBombsCounter(1);
                }
                else if(!model.isRevealed(x, y)){
                    view.updateCell(x, y, "F");
                    model.changeFlag(x, y);
                    if (model.isBomb(x, y)){
                        model.revealCell(x, y);
                        model.bombsCountChange(-1);
                    }
                    view.updateBombsCounter(-1);
                    if(model.checkVictory() && view.getBombsRemaining() == 0){
                        view.showVictory();
                    }
                }
            }
            else{
                System.out.println("Wrong command! Try again");
            }
        }
        else{
            System.out.println("Wrong command! Try again");
        }
    }

    private void openCells(int x, int y, GameModel model){
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;

                if(nx >= 0 && ny >= 0 && nx < model.getFieldHeight() && ny < model.getFieldWidth()) {
                    if (model.isRevealed(nx, ny) || model.isFlagged(nx, ny)) continue;

                    if (model.isBomb(nx, ny)) continue;

                    model.revealCell(nx, ny);
                    int bombCount = model.countNearBombs(nx, ny);

                    if (bombCount > 0) {
                        view.updateCell(nx, ny, Integer.toString(bombCount));
                    } else {
                        view.updateCell(nx, ny, "0");
                        openCells(nx, ny, model);
                    }
                }
            }
        }
    }

    public void listenFailureAction(){
        String commandline = input.nextLine().trim();
        if (commandline.equals("Restart")){
            model.stopTimer();
            model.launchGame();
            view.showGame();
        } else if (commandline.equals("Main Menu")) {
            model.stopTimer();
            view.showConsoleMode();
        }
        else{
            model.stopTimer();
            model.exitFromApp();
        }
    }

    public static String listenName(){
        String commandline = input.nextLine().trim();
        if (!commandline.isEmpty())
            return commandline;
        return "anonim";
    }



}
