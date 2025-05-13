package org.carfactory.model.factoryinit;

import org.carfactory.model.dealercenter.ThreadpoolDealers;
import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.factory.Car;
import org.carfactory.model.factory.ControllerOfStorageCar;
import org.carfactory.model.factory.ThreadpoolWorkers;
import org.carfactory.model.parseexceptions.ParseConfigException;
import org.carfactory.model.suppliers.*;
import org.carfactory.threadpool.Threadpool;
import org.carfactory.view.GUIView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        if (Boolean.parseBoolean(configMap.get("LogSales"))) {
            setupLogger();
        }

        Storage<Engine> engineStorage = new Storage<>(Integer.parseInt(configMap.get("StorageEngineSize")));
        Storage<Body> bodyStorage = new Storage<>(Integer.parseInt(configMap.get("StorageBodySize")));
        Storage<Accessory> accessoryStorage = new Storage<>(Integer.parseInt(configMap.get("StorageAccessorySize")));
        Storage<Car> carStorage = new Storage<>(Integer.parseInt(configMap.get("StorageCarSize")));

        Supplier<Engine> engineSupplier = new Supplier<>(engineStorage, Engine.class);
        Supplier<Body> bodySupplier = new Supplier<>(bodyStorage, Body.class);

        ControllerOfStorageCar controller = new ControllerOfStorageCar(carStorage, Integer.parseInt(configMap.get("Workers")));

        ThreadpoolDealers dealers = new ThreadpoolDealers(Integer.parseInt(configMap.get("Dealers")), carStorage, controller);

        ThreadpoolWorkers workers = new ThreadpoolWorkers(Integer.parseInt(configMap.get("Workers")), carStorage, engineStorage, bodyStorage, accessoryStorage, controller);

        Threadpool<Accessory> accessorySuppliers = new
                Threadpool<>(Accessory.class, Integer.parseInt(configMap.get("AccessorySup")), accessoryStorage);


        Thread engineSupplierThread = new Thread(engineSupplier);
        Thread bodySupplierThread = new Thread(bodySupplier);

        Thread controllerThread = new Thread(controller);


        for(int i = 0; i < accessorySuppliers.getSuppliersCount(); i++){
            accessorySuppliers.startThread(i);
        }

        for(int i = 0; i < workers.getWorkersCount(); i++){
            workers.startThread(i);
        }

        for(int i = 0; i < dealers.getDealersCount(); i++){
            dealers.startThread(i);
        }

        engineSupplierThread.start();
        bodySupplierThread.start();
        controllerThread.start();



        GUIView view = new GUIView(engineSupplier, bodySupplier, engineStorage, bodyStorage, accessorySuppliers, accessoryStorage, workers, carStorage, dealers, controller);

        view.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                engineSupplierThread.interrupt();
                bodySupplierThread.interrupt();
                controller.stop();
                controllerThread.interrupt();
                for(int i = 0; i < accessorySuppliers.getSuppliersCount(); i++){
                    accessorySuppliers.stopThread(i);
                }
                for(int i = 0; i < workers.getWorkersCount(); i++){
                    workers.stopThread(i);
                }
                for(int i = 0; i <dealers.getDealersCount(); i++){
                    dealers.stopThread(i);
                }
                view.dispose();
            }
        });
        view.setResizable(false);
        view.setVisible(true);

        SwingUtilities.invokeLater(() -> view.initWindow());
    }


    private void exitApp(){
        System.exit(0);
    }

    private static void setupLogger(){
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("logging.properties")) {
            if (in == null) {
                System.err.println("Error: logging.properties not found on classpath!");
                return;
            }
            LogManager.getLogManager().readConfiguration(in);
            System.out.println("Logging config is loaded!");
        } catch (IOException e) {
            System.err.println("Error loading logging config: " + e.getMessage());
        }
    }

}
