package queuefeeder.producer.producer;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.producer.generator.MessageGenerator;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class MessageProducerImpl implements MessageProducer<String> {

    private final char prefix;
    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    private final String poisonPill;
    private final int messagesPerMessageType;

    public MessageProducerImpl(char prefix, ArrayBlockingQueue<String> arrayBlockingQueue, int messagesPerMessageType, String poisonPill) {
        this.prefix = prefix;
        this.arrayBlockingQueue = arrayBlockingQueue;
        this.poisonPill = poisonPill;
        this.messagesPerMessageType = messagesPerMessageType;
    }

    @Override
    public void run() {
        log.info("Thread of producer: " + Thread.currentThread().getName());
        log.info("Message producer: " + prefix + " started");
        MessageGenerator<String> messageGenerator = MessageGenerator.getStringMessageGenerator(prefix);
        try {
            for (int i = 0; i < messagesPerMessageType; i++) {
                arrayBlockingQueue.put(messageGenerator.getMessage());
            }
            log.info("producer " + prefix + " sending poison");
            arrayBlockingQueue.put(poisonPill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Message producer: " + prefix + " done");
    }
}
