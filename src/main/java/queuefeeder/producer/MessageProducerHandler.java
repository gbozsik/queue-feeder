package queuefeeder.producer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class MessageProducerHandler {

    private final int numberOfThreads;
    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    private final String poisonPill;
    @Getter
    private List<Character> prefixes;

    public MessageProducerHandler(int numberOfThreads, ArrayBlockingQueue<String> arrayBlockingQueue, String poisonPill) {
        this.numberOfThreads = numberOfThreads;
        this.arrayBlockingQueue = arrayBlockingQueue;
        this.poisonPill = poisonPill;
    }

    public void produceMessages() throws InterruptedException {
        prefixes = getPrefixList(numberOfThreads);
        List<MessageProducer> messageProducers = getMessageProducers();
        MessageProducerExecutorService messageProducerExecutorService = new MessageProducerExecutorService();
        messageProducerExecutorService.startProcessorsOnDifferentThreads(messageProducers);
    }

    private List<MessageProducer> getMessageProducers() {
        List<MessageProducer> messageProducers = new ArrayList<>();
        for (Character prefix : prefixes) {
            messageProducers.add(new MessageProducer(prefix, arrayBlockingQueue, poisonPill));
        }
        return messageProducers;
    }

    private List<Character> getPrefixList(int numberOfThreads) {
        List<Character> prefixes = new ArrayList<>();
        if (numberOfThreads > 26) {
            throw new IllegalArgumentException("Threads maximum number is 26");
        }
        for (int prefixCharPosition = 0; prefixCharPosition < numberOfThreads; prefixCharPosition++) {
            char prefix = (char) (prefixCharPosition + 65);
            prefixes.add(prefix);
        }
        return prefixes;
    }
}
