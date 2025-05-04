package org.carfactory.view;

import org.carfactory.controller.GUIListener;
import org.carfactory.model.dealercenter.ThreadpoolDealers;
import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.factory.Car;
import org.carfactory.model.factory.ControllerOfStorageCar;
import org.carfactory.model.factory.ThreadpoolWorkers;
import org.carfactory.model.suppliers.Storage;
import org.carfactory.model.suppliers.Supplier;

import org.carfactory.threadpool.Threadpool;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GUIView extends JFrame {
    private final GUIListener listener = new GUIListener();
    private final Supplier<Engine> engineSupplier;
    private final Supplier<Body> bodySupplier;
    private final Storage<Engine> engineStorage;
    private final Storage<Body> bodyStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Threadpool<Accessory> accessorySuppliers;
    private final ThreadpoolWorkers workers;
    private final Storage<Car> carStorage;
    private final ThreadpoolDealers dealers;
    private final ControllerOfStorageCar controller;
    private JLabel engineStorageSizeLabel;
    private JLabel bodyStorageSizeLabel;
    private JLabel accessoryStorageSizeLabel;
    private JLabel engineCreatedCounterLabel;
    private JLabel bodyCreatedCounterLabel;
    private JLabel accessoryCreatedCounterLabel;
    private JLabel carStorageSizeLabel;
    private JLabel carCreatedCounterLabel;


    public GUIView(Supplier<Engine> engineSupplier, Supplier<Body> bodySupplier, Storage<Engine> engineStorage, Storage<Body> bodyStorage,
                   Threadpool<Accessory> accessorySuppliers, Storage<Accessory> accessoryStorage, ThreadpoolWorkers workers, Storage<Car> carStorage,
                   ThreadpoolDealers dealers, ControllerOfStorageCar controller){
        super();
        this.engineSupplier = engineSupplier;
        this.bodySupplier = bodySupplier;
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.accessorySuppliers = accessorySuppliers;
        this.workers = workers;
        this.carStorage = carStorage;
        this.dealers = dealers;
        this.controller = controller;


        setSize(800, 800);
    }

    public void initWindow(){
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();

        panel.setLayout(layout);

        JLabel engineSupplierLabel = new JLabel("Delay for Engine Supplier"); //Engine Supplier

        JSlider engineSupplierSlider = new JSlider(0, 3, 0);
        engineSupplierSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                engineSupplier.setDelay(engineSupplierSlider.getValue());
            }
        });

        engineSupplierSlider.setValue(3);
        engineSupplierSlider.setMajorTickSpacing(1);
        engineSupplierSlider.setPaintTicks(true);
        engineSupplierSlider.setPaintLabels(true);

        JLabel bodySupplierLabel = new JLabel("Delay for Body Supplier"); //Body Supplier

        JSlider bodySupplierSlider = new JSlider(0, 3, 0);
        bodySupplierSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                bodySupplier.setDelay(bodySupplierSlider.getValue());
            }
        });

        bodySupplierSlider.setValue(3);
        bodySupplierSlider.setMajorTickSpacing(1);
        bodySupplierSlider.setPaintTicks(true);
        bodySupplierSlider.setPaintLabels(true);

        engineStorageSizeLabel = new JLabel("0");
        bodyStorageSizeLabel = new JLabel("0");

        engineStorageSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        engineStorageSizeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        bodyStorageSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bodyStorageSizeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        engineStorage.addSizeListener(this::updateSizeEngine);
        bodyStorage.addSizeListener(this::updateSizeBody);

        JLabel engineSizeStorageTextLabel = new JLabel("Engine Storage: ");
        JLabel bodySizeStorageTextLabel = new JLabel("Body Storage:");

        JLabel bodySupplierCounterTextLabel = new JLabel("Created bodies:");
        JLabel engineSupplierCounterTextLabel = new JLabel("Created engines:");

        engineCreatedCounterLabel = new JLabel("0");
        bodyCreatedCounterLabel = new JLabel("0");

        engineCreatedCounterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        engineCreatedCounterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        bodyCreatedCounterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bodyCreatedCounterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        engineSupplier.addCreatedListener(this::updateCreatedEngine);
        bodySupplier.addCreatedListener(this::updateCreatedBody);


        JLabel accessorySuplierLabel = new JLabel("Delay for Accessory Suppliers"); //Accessory Supplier

        JSlider accessorySupplierSlider = new JSlider(0, 3, 0);
        accessorySupplierSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                for(int i = 0; i < accessorySuppliers.getSuppliersCount(); i++) {
                    accessorySuppliers.setDelayForAll(accessorySupplierSlider.getValue());
                }
            }
        });

        accessorySupplierSlider.setValue(3);
        accessorySupplierSlider.setMajorTickSpacing(1);
        accessorySupplierSlider.setPaintTicks(true);
        accessorySupplierSlider.setPaintLabels(true);

        accessoryStorageSizeLabel = new JLabel("0");
        accessoryStorageSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accessoryStorageSizeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        accessoryStorage.addSizeListener(this::updateSizeAccessory);

        JLabel accessorySizeStorageTextLabel = new JLabel("Accessory Storage:");

        JLabel accessorySupplierCounterTextLabel = new JLabel("Created Accessory:");

        accessoryCreatedCounterLabel = new JLabel("0");

        accessoryCreatedCounterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accessoryCreatedCounterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        accessorySuppliers.addCreatedListener(this::updateCreatedAccessory);

        carStorageSizeLabel = new JLabel("0");
        carStorageSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        carStorageSizeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        carStorage.addSizeListener(this::updateSizeCar);

        JLabel carSizeStorageTextLabel = new JLabel("Car Storage:");

        JLabel carCreatedCounterTextLabel = new JLabel("Created Cars:");

        carCreatedCounterLabel = new JLabel("0");
        carCreatedCounterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        carCreatedCounterLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        workers.addCreatedListener(this::updateCreatedCar);

        JLabel dealersLabel = new JLabel("Delay for Dealers");

        JSlider dealersSlider = new JSlider(0, 3, 0);
        dealersSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                for(int i = 0; i < dealers.getDealersCount(); i++) {
                    dealers.setDelayForAll(dealersSlider.getValue());
                }
            }
        });

        dealersSlider.setValue(3);
        dealersSlider.setMajorTickSpacing(1);
        dealersSlider.setPaintTicks(true);
        dealersSlider.setPaintLabels(true);

        JLabel tasksLabel = new JLabel("Amount of Tasks: ");

        panel.add(bodySupplierLabel);
        panel.add(engineSupplierLabel);
        panel.add(engineSupplierSlider);
        panel.add(bodySupplierSlider);
        panel.add(engineStorageSizeLabel);
        panel.add(bodyStorageSizeLabel);
        panel.add(engineSizeStorageTextLabel);
        panel.add(bodySizeStorageTextLabel);
        panel.add(bodySupplierCounterTextLabel);
        panel.add(engineSupplierCounterTextLabel);
        panel.add(bodyCreatedCounterLabel);
        panel.add(engineCreatedCounterLabel);
        panel.add(accessorySuplierLabel);
        panel.add(accessorySupplierSlider);
        panel.add(accessoryStorageSizeLabel);
        panel.add(accessorySizeStorageTextLabel);
        panel.add(accessorySupplierCounterTextLabel);
        panel.add(accessoryCreatedCounterLabel);
        panel.add(carStorageSizeLabel);
        panel.add(carSizeStorageTextLabel);
        panel.add(carCreatedCounterTextLabel);
        panel.add(carCreatedCounterLabel);
        panel.add(dealersLabel);
        panel.add(dealersSlider);
        panel.add(tasksLabel);

        layout.putConstraint(SpringLayout.WEST, engineSupplierLabel, 20, SpringLayout.WEST, panel); //Label for Engine Supplier
        layout.putConstraint(SpringLayout.NORTH, engineSupplierLabel, 40, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.NORTH, engineSupplierSlider, 20, SpringLayout.NORTH, engineSupplierLabel); //Slider for Engine Supplier
        layout.putConstraint(SpringLayout.WEST, engineSupplierSlider, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.NORTH, bodySupplierLabel, 60, SpringLayout.SOUTH, engineSupplierSlider); //Label for Body Supplier
        layout.putConstraint(SpringLayout.WEST, bodySupplierLabel, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.NORTH, bodySupplierSlider, 10, SpringLayout.SOUTH, bodySupplierLabel); //Slider for Body Supplier
        layout.putConstraint(SpringLayout.WEST, bodySupplierSlider, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.WEST, engineStorageSizeLabel, 40, SpringLayout.EAST, engineSupplierSlider); //Counter for Engine Storage
        layout.putConstraint(SpringLayout.NORTH, engineStorageSizeLabel, 80, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, bodyStorageSizeLabel, 40, SpringLayout.EAST, bodySupplierSlider); //Counter for Body Storage
        layout.putConstraint(SpringLayout.NORTH, bodyStorageSizeLabel, 200, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, engineSizeStorageTextLabel, 38, SpringLayout.EAST, engineSupplierSlider); //Text  for Engine Storage
        layout.putConstraint(SpringLayout.SOUTH, engineSizeStorageTextLabel, 60, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, bodySizeStorageTextLabel, 38, SpringLayout.EAST, bodySupplierSlider); //Text for Body Storage
        layout.putConstraint(SpringLayout.SOUTH, bodySizeStorageTextLabel, 180, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, bodySupplierCounterTextLabel, 40, SpringLayout.EAST, bodySizeStorageTextLabel); //Text Created bodies: for Body Supplier
        layout.putConstraint(SpringLayout.SOUTH, bodySupplierCounterTextLabel, 180, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, engineSupplierCounterTextLabel, 40, SpringLayout.EAST, engineSizeStorageTextLabel); //Text Created engines: for Engine Supplier
        layout.putConstraint(SpringLayout.SOUTH, engineSupplierCounterTextLabel, 60, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, bodyCreatedCounterLabel, 40, SpringLayout.EAST, bodySizeStorageTextLabel);
        layout.putConstraint(SpringLayout.NORTH, bodyCreatedCounterLabel, 20, SpringLayout.SOUTH, bodySupplierCounterTextLabel);

        layout.putConstraint(SpringLayout.WEST, engineCreatedCounterLabel, 40, SpringLayout.EAST, engineSizeStorageTextLabel);
        layout.putConstraint(SpringLayout.NORTH, engineCreatedCounterLabel, 20, SpringLayout.SOUTH, engineSupplierCounterTextLabel);

        layout.putConstraint(SpringLayout.NORTH, accessorySuplierLabel, 60, SpringLayout.SOUTH, bodySupplierSlider); // Label for Accessory Supplier
        layout.putConstraint(SpringLayout.WEST, accessorySuplierLabel, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.NORTH, accessorySupplierSlider, 10, SpringLayout.SOUTH, accessorySuplierLabel); //Slider for Accessory Supplier
        layout.putConstraint(SpringLayout.WEST, accessorySupplierSlider, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.WEST, accessoryStorageSizeLabel, 40, SpringLayout.EAST, accessorySupplierSlider); //Counter for Accessory Storage
        layout.putConstraint(SpringLayout.NORTH, accessoryStorageSizeLabel, 90, SpringLayout.SOUTH, bodySupplierSlider);

        layout.putConstraint(SpringLayout.WEST, accessorySizeStorageTextLabel, 40, SpringLayout.EAST, accessorySupplierSlider); //Text for Accessory Storage
        layout.putConstraint(SpringLayout.NORTH, accessorySizeStorageTextLabel, 60, SpringLayout.SOUTH, bodySupplierSlider);

        layout.putConstraint(SpringLayout.WEST, accessorySupplierCounterTextLabel, 40, SpringLayout.EAST, accessorySizeStorageTextLabel); //Text Created accessory:
        layout.putConstraint(SpringLayout.NORTH, accessorySupplierCounterTextLabel, 60, SpringLayout.SOUTH, bodySupplierSlider);                      // for Accessory Supplier

        layout.putConstraint(SpringLayout.WEST, accessoryCreatedCounterLabel, 40, SpringLayout.EAST, accessorySizeStorageTextLabel);
        layout.putConstraint(SpringLayout.NORTH, accessoryCreatedCounterLabel, 90, SpringLayout.SOUTH, bodySupplierSlider);

        layout.putConstraint(SpringLayout.WEST, carStorageSizeLabel, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, carStorageSizeLabel, 10, SpringLayout.SOUTH, carSizeStorageTextLabel);

        layout.putConstraint(SpringLayout.WEST, carSizeStorageTextLabel, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, carSizeStorageTextLabel, 60, SpringLayout.SOUTH, accessorySupplierSlider);

        layout.putConstraint(SpringLayout.WEST, carCreatedCounterTextLabel, 40, SpringLayout.EAST, carSizeStorageTextLabel);
        layout.putConstraint(SpringLayout.NORTH, carCreatedCounterTextLabel, 0, SpringLayout.NORTH, carSizeStorageTextLabel);

        layout.putConstraint(SpringLayout.NORTH, carCreatedCounterLabel, 0, SpringLayout.NORTH, carStorageSizeLabel);
        layout.putConstraint(SpringLayout.WEST, carCreatedCounterLabel, 0, SpringLayout.WEST, carCreatedCounterTextLabel);

        layout.putConstraint(SpringLayout.WEST, dealersLabel, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, dealersLabel, 60, SpringLayout.SOUTH, carCreatedCounterLabel);

        layout.putConstraint(SpringLayout.WEST, dealersSlider, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, dealersSlider, 10, SpringLayout.SOUTH, dealersLabel);

        layout.putConstraint(SpringLayout.WEST, tasksLabel, 0, SpringLayout.WEST, accessorySizeStorageTextLabel);
        layout.putConstraint(SpringLayout.NORTH, tasksLabel, 0, SpringLayout.NORTH, carCreatedCounterTextLabel);

        this.add(panel);
    }

    private void updateSizeEngine(){
        engineStorageSizeLabel.setText(Integer.toString(engineStorage.getNowSize()));
    }

    private void updateSizeBody(){
        bodyStorageSizeLabel.setText(Integer.toString(bodyStorage.getNowSize()));
    }

    private void updateSizeAccessory(){ accessoryStorageSizeLabel.setText(Integer.toString(accessoryStorage.getNowSize()));}


    private void updateCreatedEngine(){
        engineCreatedCounterLabel.setText(Integer.toString(engineSupplier.getCreatedParts()));
    }

    private void updateCreatedBody(){
        bodyCreatedCounterLabel.setText(Integer.toString(bodySupplier.getCreatedParts()));
    }

    private void updateCreatedAccessory(){
        accessoryCreatedCounterLabel.setText(Integer.toString((accessorySuppliers.getCreatedParts())));
    }

    private void updateSizeCar(){
        carStorageSizeLabel.setText(Integer.toString(carStorage.getNowSize()));
    }

    private void updateCreatedCar(){
        carCreatedCounterLabel.setText(Integer.toString(workers.getCreatedParts()));
    }
}
