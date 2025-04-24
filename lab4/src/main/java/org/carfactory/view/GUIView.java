package org.carfactory.view;

import org.carfactory.controller.GUIListener;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.factoryinit.CarFactory;
import org.carfactory.model.suppliers.Storage;
import org.carfactory.model.suppliers.Supplier;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

        JLabel engineSupplierLabel = new JLabel("Delay for Engine Supplier"); //Engine Supplier

        JSlider engineSupplierSlider = new JSlider(0, 3, 0);
        engineSupplierSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                engineSupplier.setDelay(engineSupplierSlider.getValue());
            }
        });

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

        bodySupplierSlider.setMajorTickSpacing(1);
        bodySupplierSlider.setPaintTicks(true);
        bodySupplierSlider.setPaintLabels(true);



        panel.add(bodySupplierLabel);
        panel.add(engineSupplierLabel);
        panel.add(engineSupplierSlider);
        panel.add(bodySupplierSlider);


        layout.putConstraint(SpringLayout.WEST, engineSupplierLabel, 20, SpringLayout.WEST, panel); //Label for Engine Supplier
        layout.putConstraint(SpringLayout.NORTH, engineSupplierLabel, 60, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.NORTH, engineSupplierSlider, 20, SpringLayout.NORTH, engineSupplierLabel); //Slider for Engine Supplier
        layout.putConstraint(SpringLayout.WEST, engineSupplierSlider, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.NORTH, bodySupplierLabel, 60, SpringLayout.SOUTH, engineSupplierSlider); //Label for Body Supplier
        layout.putConstraint(SpringLayout.WEST, bodySupplierLabel, 20, SpringLayout.WEST, panel);

        layout.putConstraint(SpringLayout.NORTH, bodySupplierSlider, 20, SpringLayout.SOUTH, bodySupplierLabel); //Label for Body Supplier
        layout.putConstraint(SpringLayout.WEST, bodySupplierSlider, 20, SpringLayout.WEST, panel);

        this.add(panel);
    }
}
