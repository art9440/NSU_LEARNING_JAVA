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
    private ActionListener buttonsListener;
    private FieldButton[][] fieldButtons;
    private int hField, wField;
    private BufferedImage origImg;
    private int bombsRemaining;
    private JLabel bombsCounterLabel;
    private JLabel timerLabel;
    private GameModel model;

    public MinesWeeper(String winTitle, int h, int w){
        super(winTitle);

        setSize(w, h);
    }

    public void initWindow(GameModel model){
        this.model = model;
        bombsRemaining = model.getBombsCount();
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //в этот topPanel в середину добавить время, которое считается отдельным потоком и в правый край, счет установленных флагов
        buttonsListener = new ButtonsListener(model, this);

        timerLabel = new JLabel("");
        topPanel.add(timerLabel);

        model.addTimeListener(this::updateTimerLabel);

        //Pause button
        JButton pause = new JButton("pause");
        pause.setActionCommand("Pause Game");
        pause.addActionListener(buttonsListener);
        topPanel.add(pause);

        //Counter of bombs
        bombsCounterLabel = new JLabel("Bombs: " + bombsRemaining);
        topPanel.add(bombsCounterLabel);

        add(topPanel, BorderLayout.NORTH);

        //Initialization field
        hField = model.getFieldHeight();
        wField = model.getFieldWidth();
        JPanel gamePanel = new JPanel(new GridLayout(hField, wField));
        gamePanel.setPreferredSize(new Dimension(wField * 30, hField * 30)); // Фиксируем размер поля
        gamePanel.setMaximumSize(gamePanel.getPreferredSize());
        gamePanel.setMinimumSize(gamePanel.getPreferredSize());

        //making field of mine sweeper
        fieldButtons = new FieldButton[hField][wField];
        MouseListener mouseListener = new MouseListener(model, this);
        origImg = loadImg("images/none.png");
        for(int i = 0; i < hField; i++){
            for(int j = 0; j < wField; j++){
                fieldButtons[i][j] = new FieldButton(i, j);
                fieldButtons[i][j].setPreferredSize(new Dimension(30, 30));
                //if (model.checkBomb(i, j)) {
                    //fieldButtons[i][j].setBomb(true);
                //}

                fieldButtons[i][j].addMouseListener(mouseListener);
                gamePanel.add(fieldButtons[i][j]);
            }
        }

        model.startTimer();


        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        containerPanel.add(gamePanel, gbc);
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
    //resizing icon of cell
    private void updateButtonIcons() {
        for (int i = 0; i < hField; i++) {
            for (int j = 0; j < wField; j++) {
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
        pauseDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> {pauseDialog.dispose();
                                                        model.resumeTimer();
                                                        });

        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setActionCommand("Back to menu from Game");
        mainMenuButton.addActionListener(buttonsListener);

        pauseDialog.add(resumeButton);
        pauseDialog.add(mainMenuButton);

        pauseDialog.setVisible(true);
    }
    //раскрыть все бомбы при проигрыше
    public void showFailedGameDialog(){
        JDialog failedGame = new JDialog(this, "Failed game", true);
        failedGame.setSize(250, 150);
        failedGame.setLayout(new GridLayout(2, 1));
        failedGame.setLocationRelativeTo(this);
        failedGame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

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


    public void updateButton(String path, int x, int y){
        FieldButton button = fieldButtons[x][y];
        setButtonIcon(button, path);
        if (!path.equals("images/flag.png") && !path.equals("images/none.png")) {
            button.setEnabled(false);
            button.setDisabledIcon(button.getIcon());
        }


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

    public void updateBombsCounter(int update){
        bombsRemaining += update;
        bombsCounterLabel.setText("Bombs: " + bombsRemaining);
    }


    public void showVictory(){
        model.stopTimer();
        int time = model.getElapsedTime();

        int minutes = time / 60;
        int seconds = time % 60;

        // Создаем текстовое поле для ввода имени
        JTextField nameField = new JTextField();

        // Создаем панель для JOptionPane (чтобы разместить текст и поле ввода)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("You have Won!!"));
        panel.add(new JLabel(String.format("Your time: %02d:%02d", minutes, seconds)));
        panel.add(new JLabel("Write your name:"));
        panel.add(nameField);

        // Создаем кнопки Confirm и Cancel
        String[] options = {"Confirm", "Cancel"};
        int result = JOptionPane.showOptionDialog(
                this, // Родительское окно
                panel, // Контент (текст + поле ввода)
                "Win!", // Заголовок окна
                JOptionPane.DEFAULT_OPTION, // Тип диалогового окна
                JOptionPane.INFORMATION_MESSAGE, // Иконка
                null, // Кастомная иконка (null = стандартная)
                options, // Кнопки
                options[0] // Кнопка по умолчанию
        );

        if (result == 0){
            model.addToHighScores(nameField.getText().trim(), time);
            this.dispose();

        }
    }


    public void updateTimerLabel() {
        SwingUtilities.invokeLater(() -> {
            int time = model.getElapsedTime();
            int minutes = time / 60;
            int seconds = time % 60;
            timerLabel.setText(String.format("Время: %02d:%02d", minutes, seconds));
        });
    }

    public int getBombsRemaining(){
        return bombsRemaining;
    }


}
