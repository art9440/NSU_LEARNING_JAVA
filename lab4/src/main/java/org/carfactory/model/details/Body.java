package org.carfactory.model.details;

import java.util.concurrent.atomic.AtomicInteger;

public class Body extends Part{
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    public Body(){
        super(idGenerator.getAndIncrement());
    }
}
