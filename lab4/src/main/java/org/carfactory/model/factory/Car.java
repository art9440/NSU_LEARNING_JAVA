package org.carfactory.model.factory;

import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;

public class Car {
    private final int id;
    private final Engine engine;
    private final Body body;
    private final Accessory accessory;


    public Car(Engine engine, Body body, Accessory accessory, int id){
        this.id = id;
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
    }
}
