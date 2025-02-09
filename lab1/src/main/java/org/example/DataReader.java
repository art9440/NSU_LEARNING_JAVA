package org.example;

import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

public class DataReader {
    int wordsCount = 0;
    Map<String, Integer> mapWords = new HashMap<>();

    private void workWithStrings(String line){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find())
        {
            wordsCount++;
            String word = matcher.group().toLowerCase();
            mapWords.put(word, mapWords.getOrDefault(word, 0) + 1);
        }

        mapWords.forEach((word, count) -> System.out.println(word + ": " + count));
    }

    public void readFromFile(String inputFile){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFile)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Файл не найден в resources: " + inputFile);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line + "||");
                workWithStrings(line);
            }
            //System.out.println("Working...");
        }
        catch (IOException e)
        {
            System.err.println("Error while reading file:" + e.getLocalizedMessage());
        }
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public Map<String, Integer> getMapWords() {
        return mapWords;
    }


}
