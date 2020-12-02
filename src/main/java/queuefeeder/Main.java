package queuefeeder;

import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.MessageProcessorHandler;
import queuefeeder.producer.MessageProducerHandler;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(7);
    private LinkedBlockingQueue<String> stringLinkedBlockingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        int numberOfMessageProducer = 4;
        int numberOfMessageProcessor = 2;
        new Main().feedingHandler(numberOfMessageProducer, numberOfMessageProcessor);
    }

    private void feedingHandler(int numberOfMessageProducer, int numberOfMessageProcessors) throws InterruptedException {
        String poisonPill = "stop";
        MessageProducerHandler messageProducerHandler = new MessageProducerHandler(numberOfMessageProducer, arrayBlockingQueue, poisonPill);
        messageProducerHandler.produceMessages();
        List<Character> messageTypes = messageProducerHandler.getPrefixes();

        List<MessageProcessor> messageProcessors = new MessageProcessorHandler(numberOfMessageProcessors, messageTypes).getMessageProcessorList();

        Dispatcher dispatcher = new Dispatcher(arrayBlockingQueue, stringLinkedBlockingQueue, messageProcessors);
        dispatcher.dispatch(poisonPill, numberOfMessageProducer);

        dumpResults(messageProcessors);

        System.out.println("ExecutorService shutdown");
    }

    private void dumpResults(List<MessageProcessor> messageProcessors) {
        AtomicInteger count = new AtomicInteger();
        stringLinkedBlockingQueue.iterator().forEachRemaining(selectedTabName -> {
            count.getAndIncrement();
            System.out.print(selectedTabName + " ");
        });
        messageProcessors.forEach(messageProcessor -> System.out.println("\n" + messageProcessor.getPrefixesInString() + messageProcessor.getProcessedMessageCount()));
    }
}
