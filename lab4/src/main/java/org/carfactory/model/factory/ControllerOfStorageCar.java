package org.carfactory.model.factory;

import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerOfStorageCar implements Runnable{
    private final BlockingQueue<Object> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    public synchronized void notifySold(){
        taskQueue.offer(new Object());
        notifyTaskListener();
    }

    public Object getBuildTask() throws InterruptedException{
        return taskQueue.take();
    }

    public synchronized int getTaskCount() {
        return taskQueue.size();
    }

    public void stop() {
        running = false;
        Thread.currentThread().interrupt(); // завершить поток
    }

    @Override
    public void run(){
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void addTaskListener(Runnable listener){
        this.listener = listener;
    }

    private void notifyTaskListener(){
        listener.run();
    }
}
