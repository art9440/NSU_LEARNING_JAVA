package org.minesweeper.GUIView;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Settings extends JFrame {
    private JPanel panel;
    private BoxLayout boxLayoutButtons, boxLayoutText;
    private JButton backToMenu, confirm, defaultB;
    private final int width, height;
    private ActionListener buttonsListener;
    private JTextField heightField, widthField, bombsField;


    public Settings(String winTitle, int w, int h){
        super(winTitle);
        width = w;
        height = h;

        setSize(width, height);
    }

    public void initWindow(GameModel model){
        panel = new JPanel(new VerticalLayout());
        buttonsListener = new ButtonsListener(model, this);




        backToMenu = new JButton("Back");

        confirm = new JButton("Confirm");
        confirm.setEnabled(false);

        defaultB = new JButton("Default");

        panel.add(backToMenu);

        backToMenu.setActionCommand("Back to menu");
        defaultB.setActionCommand("Default Settings");
        confirm.setActionCommand("Confirm Settings");

        backToMenu.addActionListener(buttonsListener);
        defaultB.addActionListener(buttonsListener);
        confirm.addActionListener(buttonsListener);

        heightField = new JTextField(10);
        heightField.setToolTipText("Add Height of the field");
        widthField = new JTextField(10);
        widthField.setToolTipText("Add Width of the field");
        bombsField = new JTextField(10);
        bombsField.setToolTipText("Add amount of Bombs");

        KeyAdapter confirmChecker = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateFields();
            }
        };

        heightField.addKeyListener(confirmChecker);
        widthField.addKeyListener(confirmChecker);
        bombsField.addKeyListener(confirmChecker);

        panel.add(new JLabel("Height :"));
        panel.add(heightField);
        panel.add(new JLabel("Width :"));
        panel.add(widthField);
        panel.add(new JLabel("Bombs :"));
        panel.add(bombsField);

        panel.add(confirm);
        panel.add(defaultB);

        getContentPane().add(panel);

    }

    private void validateFields() {
        boolean allValid = true;

        // Проверяем каждое поле
        JTextField[] fields = {heightField, widthField, bombsField};
        for (JTextField field : fields) {
            String text = field.getText();
            // Проверяем, что строка состоит только из цифр и не более 4 символов
            if (text.length() > 4 || !text.matches("\\d*")) {
                allValid = false;
                break;
            }
        }

        // Включаем кнопку, если все поля корректно заполнены
        if (allValid) {
            confirm.setEnabled(allValid);
        }
    }
}
