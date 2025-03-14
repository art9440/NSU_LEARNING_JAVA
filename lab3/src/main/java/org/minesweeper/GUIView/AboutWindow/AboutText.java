package org.minesweeper.GUIView.AboutWindow;

import javax.swing.*;
import java.awt.*;

public class AboutText extends JPanel {

    public void paint(Graphics g){
        g.drawString("This is a mine Sweeper. " +
                "Your goal is to sweep all bombs on the field." +
                "Use Right Click to set the flag on cell." +
                "Use Left Click to open cell.", 100, 10);
    }
}
