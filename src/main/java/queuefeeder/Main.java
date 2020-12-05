package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.dispatcher.Dispatcher;
import queuefeeder.dispatcher.DispatcherImpl;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorProvider;
import queuefeeder.processor.MessageProcessorProviderImpl;
import queuefeeder.producer.MessageProducerHandler;
import queuefeeder.producer.MessageProducerHandlerImpl;

import java.util.List;
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
        LinkedBlockingQueue<String> stringLinkedBlockingQueue = new LinkedBlockingQueue<>();

        MessageProducerHandler messageProducerHandler = new MessageProducerHandlerImpl(numberOfMessageProducer, arrayBlockingQueue, messagesPerMessageType, poisonPill);
        Dispatcher dispatcher = new DispatcherImpl(arrayBlockingQueue, stringLinkedBlockingQueue);
        MessageProcessorProvider messageProcessorProvider = new MessageProcessorProviderImpl();

        FeedingParams feedingParams = FeedingParams.builder()
                .numberOfMessageProducer(numberOfMessageProducer)
                .numberOfMessageProcessor(numberOfMessageProcessor)
                .messagesPerMessageType(messagesPerMessageType)
                .arrayBlockingQueueCapacity(arrayBlockingQueueCapacity)
                .poisonPill(poisonPill)
                .build();

        QueueFeeder queueFeeder = new QueueFeeder(messageProducerHandler, dispatcher, messageProcessorProvider);
        queueFeeder.feed(feedingParams);
        queueFeeder.dumpResults(dispatcher);
    }
}
