package queuefeeder.producer.producer;

import java.util.concurrent.ArrayBlockingQueue;

public interface MessageProducer<T> extends Runnable {

    static MessageProducer<String> getStringMessageProducer(char prefix, ArrayBlockingQueue<String> arrayBlockingQueue,
                                                            int messagesPerMessageType, String poisonPill) {
        return new MessageProducerImpl(prefix, arrayBlockingQueue, messagesPerMessageType, poisonPill);
    }
}
