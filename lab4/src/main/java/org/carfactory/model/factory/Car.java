package org.carfactory.model.factory;

import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.details.Part;

import java.util.concurrent.atomic.AtomicInteger;

public class Car extends Part {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Engine engine;
    private final Body body;
    private final Accessory accessory;


    public Car(Engine engine, Body body, Accessory accessory){
        super(idGenerator.getAndIncrement());
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
    }

    public String getPartID(){
        return "Body ID: " + Integer.toString(body.getID()) + "; " + "Engine ID: " + Integer.toString(engine.getID()) +
                "; " + "Accessory ID: " + Integer.toString(accessory.getID());
    }
}
