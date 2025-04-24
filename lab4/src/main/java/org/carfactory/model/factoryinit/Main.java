package org.carfactory.model.factoryinit;


import org.carfactory.controller.GUIListener;
import org.carfactory.view.GUIView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        CarFactory factory = new CarFactory();
        factory.createFactory();
    }
}