package org.carfactory.model.factoryinit;


import org.carfactory.controller.GUIListener;
import org.carfactory.view.GUIView;

public class Main {
    public static void main(String[] args) {
        GUIListener listener = new GUIListener();
        CarFactory factory = new CarFactory();
        GUIView view = new GUIView(factory, listener);

        view.initWindow();
    }
}