package queuefeeder.producer;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;

public class MessageProducer implements Runnable {

    private final char prefix;
    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    private final String poisonPill;

    public MessageProducer(char prefix, ArrayBlockingQueue<String> arrayBlockingQueue, String poisonPill) {
        this.prefix = prefix;
        this.arrayBlockingQueue = arrayBlockingQueue;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        System.out.println("Message producer: " + prefix + " started");
        MessageGenerator messageGenerator = new MessageGenerator(prefix);
        try {
            for (int suffix = 0; suffix < 10; suffix++) {
                arrayBlockingQueue.put(messageGenerator.getMessage(suffix));
            }
            System.out.println("producer " + prefix + " sending poison");
            arrayBlockingQueue.put(poisonPill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Message producer: " + prefix + " done");

    }
}
