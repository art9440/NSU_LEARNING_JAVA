package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.nio.file.*;


public class CSVWriter {
    public static void writeToCSV(String outputFile, int wordsCount, Map<String, Integer> mapWords){
        Path path = Paths.get(outputFile);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){

            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(mapWords.entrySet());
            sortedList.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

            writer.write("Word, Count, Frequency");
            writer.newLine();

            for (Map.Entry<String, Integer> entry : sortedList) {
                writer.write(entry.getKey() + "," + entry.getValue() + "," + ((double) entry.getValue() /
                        (double) wordsCount) * 100);
                writer.newLine();
            }

            System.out.println("Writing to CSV is done");
        }
        catch (IOException e)
        {
            System.err.println("Error when writing to CSV: " + e.getMessage());
        }

    }
}
