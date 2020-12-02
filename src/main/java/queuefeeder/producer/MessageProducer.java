package queuefeeder.producer;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class MessageProducer implements Runnable {

    private final char prefix;
    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    private final String poisonPill;
    private final int messagesPerMessageType;

    public MessageProducer(char prefix, ArrayBlockingQueue<String> arrayBlockingQueue, int messagesPerMessageType, String poisonPill) {
        this.prefix = prefix;
        this.arrayBlockingQueue = arrayBlockingQueue;
        this.poisonPill = poisonPill;
        this.messagesPerMessageType = messagesPerMessageType;
    }

    @Override
    public void run() {
        log.info("Message producer: " + prefix + " started");
        MessageGenerator messageGenerator = new MessageGenerator(prefix);
        try {
            for (int suffix = 0; suffix < messagesPerMessageType; suffix++) {
                arrayBlockingQueue.put(messageGenerator.getMessage(suffix));
            }
            log.info("producer " + prefix + " sending poison");
            arrayBlockingQueue.put(poisonPill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Message producer: " + prefix + " done");
    }
}
