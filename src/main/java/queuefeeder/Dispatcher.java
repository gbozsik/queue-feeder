package queuefeeder;

import queuefeeder.processor.MessageProcessor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Dispatcher {

    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    private final LinkedBlockingQueue<String> linkedBlockingQueue;
    private final List<MessageProcessor> messageProcessors;

    public Dispatcher(ArrayBlockingQueue<String> arrayBlockingQueue,
                      LinkedBlockingQueue<String> linkedBlockingQueue,
                      List<MessageProcessor> messageProcessors) {
        this.arrayBlockingQueue = arrayBlockingQueue;
        this.linkedBlockingQueue = linkedBlockingQueue;
        this.messageProcessors = messageProcessors;
    }

    public void dispatch(String poisonPill, int numberOfMessageProducer) {
        int countOfPoisonPill = 0;
        while (true) {
            String messageFromBlockingQueue = arrayBlockingQueue.poll();
            if (Objects.nonNull(messageFromBlockingQueue)) {
                if (messageFromBlockingQueue.equals(poisonPill)) {
                    countOfPoisonPill++;
                    if (countOfPoisonPill == numberOfMessageProducer) {
                        return;
                    }
                }
                for (MessageProcessor messageProcessor : messageProcessors) {
                    if (messageProcessor.accept(messageFromBlockingQueue)) {
                        messageProcessor.process(messageFromBlockingQueue, linkedBlockingQueue);
                    }
                }
            }
        }
    }
}

