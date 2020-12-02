package queuefeeder;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorHandler;
import queuefeeder.producer.MessageProducerHandler;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Main {

    private ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(7);
    private LinkedBlockingQueue<String> stringLinkedBlockingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        log.info("main starts");
        int numberOfMessageProducer = 7;
        int numberOfMessageProcessor = 3;
        int messagesPerMessageType = 5;
        String poisonPill = "stop";
        new Main().feedingHandler(numberOfMessageProducer, numberOfMessageProcessor, messagesPerMessageType, poisonPill);
    }

    private void feedingHandler(int numberOfMessageProducer, int numberOfMessageProcessors, int messagesPerMessageType, String poisonPill) {
        if (numberOfMessageProcessors == 0) {
            throw new IllegalArgumentException("NumberOfMessageProcessors can not be 0");
        }
        if (numberOfMessageProducer == 0) {
            throw new IllegalArgumentException("NumberOfMessageProducer can not be 0");
        }

        MessageProducerHandler messageProducerHandler = new MessageProducerHandler(numberOfMessageProducer, arrayBlockingQueue, messagesPerMessageType, poisonPill);
        messageProducerHandler.produceMessages();
        List<Character> messageTypes = messageProducerHandler.getPrefixes();

        List<MessageProcessor> messageProcessors = new MessageProcessorHandler(numberOfMessageProcessors, messageTypes).getMessageProcessorList();

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
            stringBuilder.append(selectedTabName + " ");
        });
        log.info(stringBuilder.toString());
        messageProcessors.forEach(messageProcessor ->
                log.info(String.format("Processor %s processed %s times",
                        messageProcessor.getPrefixesInString(), messageProcessor.getProcessedMessageCount())));
    }
}
