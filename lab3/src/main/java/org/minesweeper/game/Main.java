package org.minesweeper.game;

public class Main {
    public static void main(String[] args) {
        GameModel model = new GameModel(args[0]);
        model.launchApp();
    }
}