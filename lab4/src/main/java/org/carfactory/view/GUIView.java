package org.carfactory.view;

import org.carfactory.controller.GUIListener;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.factoryinit.CarFactory;
import org.carfactory.model.suppliers.Storage;
import org.carfactory.model.suppliers.Supplier;

import javax.swing.*;
import java.awt.event.ActionListener;

public class GUIView extends JFrame {
    private final GUIListener listener = new GUIListener();
    private final Supplier<Engine> engineSupplier;
    private final Supplier<Body> bodySupplier;
    private final Storage<Engine> engineStorage;
    private final Storage<Body> bodyStorage;

    public GUIView(Supplier<Engine> engineSupplier, Supplier<Body> bodySupplier, Storage<Engine> engineStorage, Storage<Body> bodyStorage){
        super();
        this.engineSupplier = engineSupplier;
        this.bodySupplier = bodySupplier;
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;


        setSize(800, 800);
    }

    public void initWindow(){
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();

        panel.setLayout(layout);

        JLabel engineSupplierLabel = new JLabel("Delay for Engine Supplier");

        panel.add(engineSupplierLabel);

        layout.putConstraint(SpringLayout.WEST, engineSupplierLabel, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, engineSupplierLabel, 60, SpringLayout.NORTH, panel);

        this.add(panel);
    }
}
