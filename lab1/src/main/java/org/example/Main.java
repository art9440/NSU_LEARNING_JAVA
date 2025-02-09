package org.example;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            if (args.length < 2){
                throw new IllegalArgumentException("Not enough arguments!");
            }

            String inputFile = args[0];
            String outputFile = args[1];



            DataReader textData = new DataReader();

            textData.readFromFile(inputFile);
            CSVWriter.writeToCSV(outputFile, textData.getWordsCount(), textData.getMapWords());


        } catch (IllegalArgumentException e){
            System.out.println("Error " + e.getMessage());
        }
    }
}