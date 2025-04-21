package org.carfactory.model.factory;

import java.util.ArrayList;
import java.util.List;

public class StorageCar {
    private final int size;
    private List<Car> storage = new ArrayList<>();

    public StorageCar(int size){
        this.size = size;
    }


}
