package queuefeeder.dispatcher;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.processor.MessageProcessor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DispatcherImpl implements Dispatcher {

    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    private final LinkedBlockingQueue<String> linkedBlockingQueue;
    private List<MessageProcessor> messageProcessors;
//    private final List<MessageProcessor> messageProcessors;

    public DispatcherImpl(ArrayBlockingQueue<String> arrayBlockingQueue,
                          LinkedBlockingQueue<String> linkedBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
        this.linkedBlockingQueue = linkedBlockingQueue;
    }

    public void dispatch(String poisonPill, int numberOfMessageProducer, List<MessageProcessor> messageProcessors) {
        this.messageProcessors = messageProcessors;
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

    @Override
    public void dumpResults() {
        AtomicInteger count = new AtomicInteger();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nContent of LinkedQueue: \n");
        linkedBlockingQueue.iterator().forEachRemaining(selectedTabName -> {
            count.getAndIncrement();
            stringBuilder.append(selectedTabName).append(" ");
        });
        log.info(stringBuilder.toString());
        messageProcessors.forEach(messageProcessor ->
                log.info(String.format("Processor %s processed %s times",
                        messageProcessor.getPrefixesInString(), messageProcessor.getProcessedMessageCount())));
    }
}

