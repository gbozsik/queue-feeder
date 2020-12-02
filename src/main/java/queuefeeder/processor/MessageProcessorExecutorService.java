package queuefeeder.processor;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MessageProcessorExecutorService {

    public void startProcessorsOnDifferentThreads(List<MessageProcessor> messageProcessors) {
        ExecutorService executorService = Executors.newFixedThreadPool(messageProcessors.size());
        messageProcessors.forEach(messageProcessor -> {
            System.out.println("Processor " + messageProcessor.getPrefixesInString() + " started");
            executorService.submit(() -> messageProcessor);
        });
        executorService.shutdown();
    }
}
