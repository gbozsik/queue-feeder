package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.dispatcher.Dispatcher;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorProvider;
import queuefeeder.producer.MessageProducer;
import queuefeeder.producer.MessageProducerHandler;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class QueueFeeder {

    private final Dispatcher dispatcher;
    private final MessageProducerHandler messageProducerHandler;
    private final MessageProcessorProvider messageProcessorProvider;

    public QueueFeeder(MessageProducerHandler messageProducerHandler, Dispatcher dispatcher, MessageProcessorProvider messageProcessorProvider) {
        this.messageProducerHandler = messageProducerHandler;
        this.dispatcher = dispatcher;
        this.messageProcessorProvider = messageProcessorProvider;
    }

    void feed(FeedingParams feedingParams) {
        if (feedingParams.getNumberOfMessageProcessor() == 0) {
            throw new IllegalArgumentException("NumberOfMessageProcessors can not be 0");
        }
        if (feedingParams.getNumberOfMessageProducer() == 0) {
            throw new IllegalArgumentException("NumberOfMessageProducer can not be 0");
        }

        List<Character> messageTypes = messageProducerHandler.getPrefixesCharList(feedingParams.getNumberOfMessageProducer());
        List<MessageProducer> messageProducers = messageProducerHandler.getMessageProducers(messageTypes, feedingParams);
        messageProducerHandler.startProducerThreads(messageProducers);

        List<MessageProcessor> messageProcessors = messageProcessorProvider.getMessageProcessorList(feedingParams.getNumberOfMessageProcessor(), messageTypes);

        dispatcher.dispatch(feedingParams, messageProcessors);

        log.info("Feeding has been finished");
    }
}
