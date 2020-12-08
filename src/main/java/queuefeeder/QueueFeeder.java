package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.aggregator.Aggregator;
import queuefeeder.dispatcher.DispatcherExecutor;
import queuefeeder.dispatcher.DispatcherExecutorService;
import queuefeeder.dispatcher.DispatcherService;
import queuefeeder.processor.executor.MessageConsumerExecutor;
import queuefeeder.processor.service.MessageConsumerService;
import queuefeeder.producer.producer.MessageProducer;
import queuefeeder.producer.service.MessageProducerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class QueueFeeder {

    private static final int NUMBER_OF_MESSAGE_PRODUCER = 5;
    private static final int NUMBER_OF_MESSAGE_PROCESSOR = 3;
    private static final int MESSAGES_PER_MESSAGE_TYPE = 5;
    private static final int ARRAY_BLOCKING_QUEUE_CAPACITY = 7;
    private static final int NUMBER_OF_DISPATCHERS = 1;
    private static final String POISON_PILL = "stop";

    public QueueFeeder() {
    }

    void feed() {
        log.info("feed method starts on " + Thread.currentThread().getName() + " thread");
        FeedingParams<String> feedingParams = prepareFeedingParams();
        if (feedingParams.getNumberOfMessageProcessor() == 0) {
            throw new IllegalArgumentException("NumberOfMessageProcessors can not be 0");
        }
        if (feedingParams.getNumberOfMessageProducer() == 0) {
            throw new IllegalArgumentException("NumberOfMessageProducer can not be 0");
        }

        MessageProducerService<String> messageProducerService = MessageProducerService.getStringMessageProducerService(POISON_PILL);

        MessageConsumerService<String> messageConsumerService = MessageConsumerService.getStringMessageConsumerService();

        feedingParams.setMessageTypes(startMessageProducingAndGetMessageTypes(feedingParams, messageProducerService));

        List<MessageConsumerExecutor<String>> messageConsumerExecutors =
                messageConsumerService.getMessageConsumerExecutorList(feedingParams);

        List<DispatcherExecutor<String>> dispatcherExecutors = getDispatchers(feedingParams, messageConsumerExecutors);
        DispatcherExecutorService.startDispatcherExecutorsOnDifferentThreads(dispatcherExecutors);
        messageConsumerExecutors.forEach(messageConsumerExecutor -> log.info(String.format("Processor %s - processed %s times.",
                messageConsumerExecutor.getMessageConsumer().getPrefixesInString(),
                messageConsumerExecutor.getMessageConsumer().getConsumeCount())));
        log.info("Feeding has been finished");
        dumpResults(feedingParams.getLinkedBlockingQueue());
    }

    private <T> List<DispatcherExecutor<T>> getDispatchers(FeedingParams<T> feedingParams, List<MessageConsumerExecutor<T>> messageConsumerExecutors) {
        List<DispatcherExecutor<T>> dispatcherExecutors = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DISPATCHERS; i++) {
            DispatcherExecutor<T> dispatcherExecutor = new DispatcherExecutor<>(DispatcherService.getDispatcherService(feedingParams, messageConsumerExecutors));
            dispatcherExecutors.add(dispatcherExecutor);
        }
        return dispatcherExecutors;
    }

    private <T> List<Character> startMessageProducingAndGetMessageTypes(FeedingParams<T> feedingParams, MessageProducerService<T> messageProducerService) {
        List<Character> messageTypes = messageProducerService.getPrefixesCharList(feedingParams.getNumberOfMessageProducer());
        List<MessageProducer<T>> messageProducers = messageProducerService.getMessageProducers(messageTypes, feedingParams);
        messageProducerService.startProducerThreads(messageProducers);
        return messageTypes;
    }

    private <T> FeedingParams<T> prepareFeedingParams() {
        ArrayBlockingQueue<T> arrayBlockingQueue = new ArrayBlockingQueue<>(ARRAY_BLOCKING_QUEUE_CAPACITY);
        LinkedBlockingQueue<T> linkedBlockingQueue = new LinkedBlockingQueue<>();
        Aggregator<T> aggregator = Aggregator.getAggregator(linkedBlockingQueue);

        FeedingParams<T> feedingParams = new FeedingParams<>();
        feedingParams.setNumberOfMessageProducer(NUMBER_OF_MESSAGE_PRODUCER);
        feedingParams.setNumberOfMessageProcessor(NUMBER_OF_MESSAGE_PROCESSOR);
        feedingParams.setMessagesPerMessageType(MESSAGES_PER_MESSAGE_TYPE);
//                feedingParams.setPoisonPill(POISON_PILL);
        feedingParams.setArrayBlockingQueue(arrayBlockingQueue);
        feedingParams.setLinkedBlockingQueue(linkedBlockingQueue);
        feedingParams.setAggregator(aggregator);
        return feedingParams;
    }

    private static <T> void dumpResults(LinkedBlockingQueue<T> linkedBlockingQueue) {
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
