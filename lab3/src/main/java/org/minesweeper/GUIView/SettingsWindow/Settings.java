package org.minesweeper.GUIView.SettingsWindow;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Settings extends JFrame implements TextFieldProvider{
    private JButton confirm;
    private JTextField heightField, widthField, bombsField;

    public String[] getTextField(){
        return new String[]{heightField.getText(), widthField.getText(), bombsField.getText()};
    }

    public Settings(String winTitle, int w, int h){
        super(winTitle);

        setSize(w, h);
    }

    public void initWindow(GameModel model){
        JPanel panel = new JPanel(new VerticalLayout());
        ActionListener buttonsListener = new ButtonsListener(model, this);


        JButton backToMenu = new JButton("Back");

        confirm = new JButton("Confirm");
        confirm.setEnabled(false);

        JButton defaultB = new JButton("Default");

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
//нужно починить, чтобы кнопка confirm активировалась, только при всех корректно заполненных полях
    private void validateFields() {
        String heightText = heightField.getText().trim();
        String widthText = widthField.getText().trim();
        String bombsText = bombsField.getText().trim();

        // Проверяем, что все поля содержат только цифры и не превышают 4 символа
        if (!heightText.matches("\\d{1,4}") || !widthText.matches("\\d{1,4}") || !bombsText.matches("\\d{1,4}")) {
            confirm.setEnabled(false);
            return;
        }

        // Преобразуем в числа
        int height = Integer.parseInt(heightText);
        int width = Integer.parseInt(widthText);
        int bombs = Integer.parseInt(bombsText);

        // Проверяем, что количество бомб не превышает общее количество клеток
        if (bombs > height * width) {
            confirm.setEnabled(false);
            return;
        }

        // Если все проверки пройдены, активируем кнопку
        confirm.setEnabled(true);
    }
}
