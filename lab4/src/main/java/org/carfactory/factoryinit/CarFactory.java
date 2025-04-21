package org.carfactory.factoryinit;

import org.carfactory.parseexceptions.ParseConfigException;

import java.util.HashMap;
import java.util.Map;

public class CarFactory {
    private Map<String, String> configMap = new HashMap<>();

    public void createFactory(){
        ParseConfig parser = new ParseConfig("configfile.txt");
        try {
            parser.parse(configMap);
        } catch (ParseConfigException e){
            System.out.println(e.getMessage());
            exitApp();
        }

        //создание всех потоков в виде объектов и одновременный запуск
    }

    private void exitApp(){
        System.exit(0);
    }
}
