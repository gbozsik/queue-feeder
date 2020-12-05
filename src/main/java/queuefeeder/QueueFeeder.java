package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.dispatcher.Dispatcher;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorProvider;
import queuefeeder.processor.MessageProcessorProviderImpl;
import queuefeeder.producer.MessageProducerHandler;

import java.util.List;

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

        messageProducerHandler.produceMessages();
        List<Character> messageTypes = messageProducerHandler.getPrefixes();

        List<MessageProcessor> messageProcessors = messageProcessorProvider.getMessageProcessorList(feedingParams.getNumberOfMessageProcessor(), messageTypes);

        dispatcher.dispatch(feedingParams.getPoisonPill(), feedingParams.getNumberOfMessageProducer(), messageProcessors);

        log.info("Feeding has been finished");
    }

    protected void dumpResults(Dispatcher dispatcher) {
        dispatcher.dumpResults();
    }
}
