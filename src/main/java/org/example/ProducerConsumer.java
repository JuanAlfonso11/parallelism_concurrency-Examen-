package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class ProducerConsumer {
    private static final int QUEUE_CAPACITY = 10;
    private static final int PRODUCER_COUNT = 2;
    private static final int CONSUMER_COUNT = 2;
    private static final int PRODUCE_COUNT = 100;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        Producer[] producers = new Producer[PRODUCER_COUNT];
        Consumer[] consumers = new Consumer[CONSUMER_COUNT];
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            producers[i] = new Producer(queue);
            new Thread(producers[i]).start();
        }
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers[i] = new Consumer(queue);
            new Thread(consumers[i]).start();
        }
        for (Producer producer : producers) {
            while (producer.isRunning()) {
                Thread.sleep(50);
            }
        }
        for(Consumer consumer:consumers){
            consumer.stop();
        }

        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        int total=0;
        for(Consumer consumer:consumers){
            total+=consumer.getSum();
        }
        System.out.println("Total sum: "+ total);
        System.out.println("Tiempo de ejecucion: "+(endTime-startTime)+"ms");

    }

    static class Producer implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final Random random = new Random();
        private volatile boolean running = true;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            try {
                for (int i = 0; i < PRODUCE_COUNT && running; i++) {
                    Integer number = random.nextInt(10);
                    queue.put(number);
                }
            } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                finally{
                    running = false;
                }
            }
        public boolean isRunning(){
            return running;

        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private volatile boolean running = true;
        private int sum = 0;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try{
                while (running) {
                    Integer number = queue.take();
                    System.out.println("Sumando numero: "+number);
                    sum += number;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        public void stop(){
            running = false;
        }

        public int getSum(){
            return sum;
        }
    }
}
