package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorProvider;
import queuefeeder.producer.MessageProducerHandler;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Main {

    private LinkedBlockingQueue<String> stringLinkedBlockingQueue;
    int numberOfMessageProducer = 7;
    int numberOfMessageProcessor = 3;
    int messagesPerMessageType = 5;
    int arrayBlockingQueueCapacity = 7;
    String poisonPill = "stop";

    public static void main(String[] args) {
        log.info("main starts");
        new Main().feedingHandler();
    }

    private void feedingHandler() {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(arrayBlockingQueueCapacity);
        stringLinkedBlockingQueue = new LinkedBlockingQueue<>();
        if (numberOfMessageProcessor == 0) {
            throw new IllegalArgumentException("NumberOfMessageProcessors can not be 0");
        }
        if (numberOfMessageProducer == 0) {
            throw new IllegalArgumentException("NumberOfMessageProducer can not be 0");
        }

        MessageProducerHandler messageProducerHandler = new MessageProducerHandler(numberOfMessageProducer, arrayBlockingQueue, messagesPerMessageType, poisonPill);
        messageProducerHandler.produceMessages();
        List<Character> messageTypes = messageProducerHandler.getPrefixes();

        List<MessageProcessor> messageProcessors = MessageProcessorProvider.getMessageProcessorList(numberOfMessageProcessor, messageTypes);

        Dispatcher dispatcher = new Dispatcher(arrayBlockingQueue, stringLinkedBlockingQueue, messageProcessors);
        dispatcher.dispatch(poisonPill, numberOfMessageProducer);

        dumpResults(messageProcessors);

        log.info("Feeding has been finished");
    }

    private void dumpResults(List<MessageProcessor> messageProcessors) {
        AtomicInteger count = new AtomicInteger();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nContent of LinkedQueue: \n");
        stringLinkedBlockingQueue.iterator().forEachRemaining(selectedTabName -> {
            count.getAndIncrement();
            stringBuilder.append(selectedTabName).append(" ");
        });
        log.info(stringBuilder.toString());
        messageProcessors.forEach(messageProcessor ->
                log.info(String.format("Processor %s processed %s times",
                        messageProcessor.getPrefixesInString(), messageProcessor.getProcessedMessageCount())));
    }
}
