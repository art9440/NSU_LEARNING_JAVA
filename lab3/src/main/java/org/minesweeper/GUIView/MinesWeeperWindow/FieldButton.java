package org.minesweeper.GUIView.MinesWeeperWindow;

import javax.swing.*;

public class FieldButton extends JButton{
    private int x, y;
    private boolean isbomb = false;
    private boolean isflag = false;

    public FieldButton(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setBomb(boolean des){
        isbomb = des;
    }

    public void setFlag(boolean des){
        isflag = des;
    }

    public boolean getFlag(){
        return isflag;
    }

    public boolean getBomb(){
        return isbomb;
    }


}
