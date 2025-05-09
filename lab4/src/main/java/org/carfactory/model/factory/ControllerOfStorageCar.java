package org.carfactory.model.factory;

import org.carfactory.model.suppliers.Storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerOfStorageCar implements Runnable {
    private final BlockingQueue<Object> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    private final Storage<Car> carStorage;

    public ControllerOfStorageCar(Storage<Car> carStorage, int workersCount) {
        this.carStorage = carStorage;

        // Seed initial build tasks for workers
        for (int i = 0; i < workersCount; i++) {
            taskQueue.offer(new Object());
        }
    }

    private Runnable listener = () -> {
    };


    public synchronized void notifySold() {
        int capacity = carStorage.getSize();
        int threshold = capacity / 2;
        int storageNow = carStorage.getNowSize();
        int tasksPending = taskQueue.size();
        // Number of tasks needed so that (storage + pending) reaches threshold
        int tasksNeeded = threshold - (storageNow + tasksPending);
        if (tasksNeeded > 0) {
            for (int i = 0; i < tasksNeeded; i++) {
                taskQueue.offer(new Object());
            }
        }
        // Notify GUI to update task count display
        notifyTaskListener();
    }


    public Object getBuildTask() throws InterruptedException {
        return taskQueue.take();
    }


    public synchronized int getTaskCount() {
        return taskQueue.size();
    }

    public void stop() {
        running = false;
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public void addTaskListener(Runnable listener) {
        this.listener = listener;
    }

    private void notifyTaskListener() {
        listener.run();
    }
}
