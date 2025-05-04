package org.carfactory.model.details;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine extends Part{
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    public Engine(){
        super(idGenerator.getAndIncrement());
    }
}
