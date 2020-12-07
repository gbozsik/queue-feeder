package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.dispatcher.Dispatcher;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorService;
import queuefeeder.producer.MessageProducer;
import queuefeeder.producer.MessageProducerService;

import java.util.List;

@Slf4j
public class QueueFeeder {

    private final Dispatcher dispatcher;
    private final MessageProducerService messageProducerService;
    private final MessageProcessorService messageProcessorService;

    public QueueFeeder(MessageProducerService messageProducerService, Dispatcher dispatcher, MessageProcessorService messageProcessorService) {
        this.messageProducerService = messageProducerService;
        this.dispatcher = dispatcher;
        this.messageProcessorService = messageProcessorService;
    }

    void feed(FeedingParams feedingParams) {
        if (feedingParams.getNumberOfMessageProcessor() == 0) {
            throw new IllegalArgumentException("NumberOfMessageProcessors can not be 0");
        }
        if (feedingParams.getNumberOfMessageProducer() == 0) {
            throw new IllegalArgumentException("NumberOfMessageProducer can not be 0");
        }

        List<Character> messageTypes = messageProducerService.getPrefixesCharList(feedingParams.getNumberOfMessageProducer());
        List<MessageProducer> messageProducers = messageProducerService.getMessageProducers(messageTypes, feedingParams);
        messageProducerService.startProducerThreads(messageProducers);

        List<MessageProcessor> messageProcessors = messageProcessorService.getMessageProcessorList(feedingParams.getNumberOfMessageProcessor(), messageTypes);

        dispatcher.dispatch(feedingParams, messageProcessors);
        messageProcessors.forEach(messageProcessor -> log.info(String.format("Processor %s - processed %s times.",
                messageProcessor.getPrefixesInString(), messageProcessor.getProcessedMessageCount())));
        log.info("Feeding has been finished");
    }
}
