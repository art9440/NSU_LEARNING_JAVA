package org.carfactory.view;

import org.carfactory.controller.GUIListener;
import org.carfactory.model.factoryinit.CarFactory;

import javax.swing.*;
import java.awt.event.ActionListener;

public class GUIView extends JFrame {
    private final CarFactory factory;
    private final GUIListener listener;

    public GUIView(CarFactory factory, GUIListener listener){
        super("Factory");

        this.factory = factory;
        this.listener = listener;

        setSize(800, 800);
    }
}
