package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.dispatcher.Dispatcher;
import queuefeeder.dispatcher.DispatcherImpl;
import queuefeeder.processor.MessageProcessorService;
import queuefeeder.processor.MessageProcessorServiceImpl;
import queuefeeder.producer.MessageProducerService;
import queuefeeder.producer.MessageProducerServiceImpl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Main {

    private static final int numberOfMessageProducer = 7;
    private static final int numberOfMessageProcessor = 3;
    private static final int messagesPerMessageType = 5;
    private static final int arrayBlockingQueueCapacity = 7;
    private static final String poisonPill = "stop";

    public static void main(String[] args) {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(arrayBlockingQueueCapacity);
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();

        MessageProducerService messageProducerService = MessageProducerService.getMessageProducerService();
        Dispatcher dispatcher = new DispatcherImpl();
        MessageProcessorService messageProcessorService = new MessageProcessorServiceImpl();

        FeedingParams feedingParams = FeedingParams.builder()
                .numberOfMessageProducer(numberOfMessageProducer)
                .numberOfMessageProcessor(numberOfMessageProcessor)
                .messagesPerMessageType(messagesPerMessageType)
                .poisonPill(poisonPill)
                .arrayBlockingQueue(arrayBlockingQueue)
                .linkedBlockingQueue(linkedBlockingQueue)
                .build();

        QueueFeeder queueFeeder = new QueueFeeder(messageProducerService, dispatcher, messageProcessorService);
        queueFeeder.feed(feedingParams);
        dumpResults(linkedBlockingQueue);
    }

    private static void dumpResults(LinkedBlockingQueue<String> linkedBlockingQueue) {
        AtomicInteger count = new AtomicInteger();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nContent of LinkedQueue: \n");
        linkedBlockingQueue.iterator().forEachRemaining(selectedTabName -> {
            count.getAndIncrement();
            stringBuilder.append(selectedTabName).append(" ");
        });
        log.info(stringBuilder.toString());
    }
}
