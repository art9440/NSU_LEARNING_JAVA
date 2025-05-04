package org.carfactory.model.details;

import java.util.concurrent.atomic.AtomicInteger;

public class Accessory extends Part{
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    public Accessory(){
        super(idGenerator.getAndIncrement());
    }
}
