package org.minesweeper.GUIView.MinesWeeperWindow;

import javax.swing.*;

public class FieldButton extends JButton{
    private final int x;
    private final int y;

    public FieldButton(int x, int y){
        this.x = x;
        this.y = y;
    }




    public int getXCoord() { return x; }
    public int getYCoord() { return y; }




}
