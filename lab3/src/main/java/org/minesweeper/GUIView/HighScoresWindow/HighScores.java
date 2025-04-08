package org.minesweeper.GUIView.HighScoresWindow;

import org.minesweeper.GUIView.GUIView;
import org.minesweeper.controller.ButtonsListener;
import org.minesweeper.game.GameModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HighScores extends JFrame {


    public HighScores(String winTitle, int w, int h){
        super(winTitle);

        setSize(w, h);
    }

    public void initWindow(GameModel model, GUIView view, ActionListener buttonsListener){
        JPanel buttonsPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(boxLayout);


        JButton backToMenu = new JButton("Back");

        backToMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.add(backToMenu);
        backToMenu.setActionCommand("Back to menu from hs and ab");

        backToMenu.addActionListener(buttonsListener);

        getContentPane().add(BorderLayout.NORTH, buttonsPanel);

        loadHighScores();
    }

    private void loadHighScores() {
        // Определяем путь к файлу рекордов
        Path filePath = Paths.get("highScores.csv");

        // Массив для хранения данных из CSV
        List<String[]> scoresData = new ArrayList<>();

        // Читаем CSV-файл
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                scoresData.add(line.split(",")); // Разделяем по запятой
            }
        } catch (IOException e) {
            System.err.println("Error while loading High Scores: " + e.getMessage());
            scoresData.add(new String[]{"No records found", ""}); // Если файл пустой
        }

        // Создаем модель таблицы
        String[] columnNames = {"Player", "Time (sec)", "Height", "Width"};
        DefaultTableModel tableModel = new DefaultTableModel(scoresData.toArray(new String[0][]), columnNames);
        // Таблица рекордов
        JTable scoresTable = new JTable(tableModel);

        // Добавляем таблицу в прокручиваемый контейнер
        JScrollPane scrollPane = new JScrollPane(scoresTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
