package org.carfactory.model.factoryinit;

import org.carfactory.model.factory.StorageCar;
import org.carfactory.model.parseexceptions.ParseConfigException;
import org.carfactory.model.suppliers.StorageAccessory;
import org.carfactory.model.suppliers.StorageBody;
import org.carfactory.model.suppliers.StorageEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CarFactory {
    private final Map<String, String> configMap = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public void createFactory(){
        ParseConfig parser = new ParseConfig("configfile.txt");
        try {
            parser.parse(configMap);
        } catch (ParseConfigException e){
            System.out.println(e.getMessage());
            exitApp();
        }

        if (Boolean.getBoolean(configMap.get("LogSales"))) {
            setupLogger();
        }

        StorageCar storageCar = new StorageCar(Integer.parseInt(configMap.get("StorageCarSize")));
        StorageBody storageBody = new StorageBody();
        StorageAccessory storageAccessory = new StorageAccessory();
        StorageEngine storageEngine = new StorageEngine();
        //создание всех потоков в виде объектов и одновременный запуск
    }

    private void exitApp(){
        System.exit(0);
    }

    private static void setupLogger(){
        try {
            InputStream configStream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            if (configStream == null) {
                System.err.println("Error: logging.properties not found!");
            } else {
                LogManager.getLogManager().readConfiguration(configStream);
                System.out.println("Config logging is loaded!");
            }

        } catch (IOException e) {
            System.err.println("Error logging: " + e.getMessage());
        }
    }
}
