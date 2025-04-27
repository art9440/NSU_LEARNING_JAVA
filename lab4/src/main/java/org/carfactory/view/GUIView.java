package org.carfactory.view;

import org.carfactory.controller.GUIListener;
import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.factoryinit.CarFactory;
import org.carfactory.model.suppliers.Storage;
import org.carfactory.model.suppliers.Supplier;
import org.carfactory.model.suppliers.ThreadpoolAccessorySuppliers;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIView extends JFrame {
    private final GUIListener listener = new GUIListener();
    private final Supplier<Engine> engineSupplier;
    private final Supplier<Body> bodySupplier;
    private final Storage<Engine> engineStorage;
    private final Storage<Body> bodyStorage;
    private final Storage<Accessory> accessoryStorage;
    private final ThreadpoolAccessorySuppliers<Accessory> accessorySupliers;
    private JLabel engineStorageSizeLabel;
    private JLabel bodyStorageSizeLabel;
    private JLabel accessoryStorageSizeLabel;
    private JLabel engineCreatedCounterLabel;
    private JLabel bodyCreatedCounterLabel;
    private JLabel accessoryCreatedCounterLabel;


    public GUIView(Supplier<Engine> engineSupplier, Supplier<Body> bodySupplier, Storage<Engine> engineStorage, Storage<Body> bodyStorage,
    ThreadpoolAccessorySuppliers<Accessory> accessorySupliers, Storage<Accessory> accessoryStorage){
        super();
        this.engineSupplier = engineSupplier;
        this.bodySupplier = bodySupplier;
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.accessorySupliers = accessorySupliers;


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
                accessorySupliers.setDelay(accessorySupplierSlider.getValue());
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
        accessorySupliers.addCreatedListener(this::updateCreatedAccessory);

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
        accessoryCreatedCounterLabel.setText(Integer.toString((accessorySupliers.getCreatedParts())));
    }
}
