package org.minesweeper.game;

import org.minesweeper.GUIView.GUIView;

import java.util.Arrays;

public class GameModel {
    private final String gameMode;
    private Integer fieldWidth;
    private Integer fieldHeight;
    private Integer bombsAmount;
    private Integer[][] bombs;
    private boolean[][] flags; // где стоят флаги
    private boolean[][] revealed; //правильно раскрытые бомбы


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
        for (int i = 0; i < fieldHeight; i++){
            Arrays.fill(bombs[i], 0);
        }
        flags = new boolean[fieldHeight][fieldWidth];
        revealed = new boolean[fieldHeight][fieldWidth];
        plantBombs();
        if(gameMode.equals("GUI")) {
            GUIView view = new GUIView(this);
            view.showGame();
        }
        //Бомбы раскидываются случайно после первого выбора игрока. До этого их нигде нет.
        //делаем класс который является наследником JButton для каждой ячейки поля.
        // В нем будем хранить координаты этой кнопки на поле, является ли она бомбой и стоит ли на ней флаг
        //Сделать два разных метода для левой конпки мыши, чтобы раскрыть кнопку и для правой кнопки мыши, чтобы установить флаг.
        //Если флаг и бомба - true, то устанавливаем true где-то и уменьшаем количество оставшихся бомб. Если флаг убирается, соответственно меняем
        //
    }
}
