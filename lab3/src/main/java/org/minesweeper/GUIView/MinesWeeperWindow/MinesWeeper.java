package org.minesweeper.GUIView.MinesWeeperWindow;

import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.controller.MouseListener;
import org.minesweeper.game.GameModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MinesWeeper extends JFrame implements PauseDialog{
    private final int width, height;
    private ActionListener buttonsListener;
    private JPanel topPanel, gamePanel;
    private JButton pause;
    private FieldButton[][] fieldButtons;
    private int h, w;
    private BufferedImage origImg;

    public MinesWeeper(String winTitle, int w, int h){
        super(winTitle);
        width = w;
        height = h;

        setSize(width, height);
    }

    public void initWindow(GameModel model){
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //в этот topPanel в середину добавить время, которое считается отдельным потоком и в правый край, счет установленных флагов
        buttonsListener = new ButtonsListener(model, this);

        //Кнопка паузы
        pause = new JButton("pause");
        pause.setActionCommand("Pause Game");
        pause.addActionListener(buttonsListener);
        topPanel.add(pause);
        add(topPanel, BorderLayout.NORTH);

        //Initialization field
        h = model.getFieldHeight();
        w = model.getFieldWidth();
        gamePanel = new JPanel(new GridLayout(h, w));
        fieldButtons = new FieldButton[h][w];
        MouseListener mouseListener = new MouseListener(model, this);
        origImg = loadImg("images/0.png");
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                fieldButtons[i][j] = new FieldButton(i, j);
                if (model.checkBomb(i, j)) {
                    fieldButtons[i][j].setBomb(true);
                }

                fieldButtons[i][j].addMouseListener(mouseListener);
                gamePanel.add(fieldButtons[i][j]);
            }
        }


        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px отступ со всех сторон
        containerPanel.add(gamePanel, BorderLayout.CENTER);

        add(containerPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonIcons();
            }
        });

        updateButtonIcons();
    }


    private BufferedImage loadImg(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
        } catch (IOException | NullPointerException e) {
            System.err.println("Ошибка загрузки изображения: " + path);
            return null;
        }
    }

    private void updateButtonIcons() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                JButton button = fieldButtons[i][j];
                int width = button.getWidth();
                int height = button.getHeight();

                if (width > 0 && height > 0 && origImg != null) {
                    Image scaledImg = origImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(scaledImg));
                }
            }
        }
    }


    public void showPause(){
        JDialog pauseDialog = new JDialog(this, "Pause", true);
        pauseDialog.setSize(250, 150);
        pauseDialog.setLayout(new GridLayout(2, 1));
        pauseDialog.setLocationRelativeTo(this);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> pauseDialog.dispose());

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setActionCommand("Back to menu");
        mainMenuButton.addActionListener(buttonsListener);

        pauseDialog.add(resumeButton);
        pauseDialog.add(mainMenuButton);

        pauseDialog.setVisible(true);
    }

    public void showFailedGameDialog(){
        JDialog failedGame = new JDialog(this, "Failed game", true);
        failedGame.setSize(250, 150);
        failedGame.setLayout(new GridLayout(2, 1));
        failedGame.setLocationRelativeTo(this);

        JButton restartButton = new JButton("Restart");
        restartButton.setActionCommand("Restart Game");
        restartButton.addActionListener(buttonsListener);

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setActionCommand("Back to menu");
        mainMenuButton.addActionListener(buttonsListener);

        failedGame.add(restartButton);
        failedGame.add(mainMenuButton);

        failedGame.setVisible(true);

    }

    public FieldButton getFieldButton(int x, int y) {
        return fieldButtons[x][y];
    }

    public void updateButton(String path, int x, int y){
        FieldButton button = fieldButtons[x][y];
        setButtonIcon(button, path);
        button.setEnabled(false);
        button.setDisabledIcon(button.getIcon());

    }

    private void setButtonIcon(FieldButton button, String path){
        try {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH));
                button.setIcon(icon);
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Ошибка загрузки изображения: " + path);
        }
    }




}
