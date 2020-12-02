package queuefeeder.producer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageProducerExecutorService {

    protected void startProcessorsOnDifferentThreads(List<MessageProducer> messageProducers) {
        ExecutorService executorService = Executors.newFixedThreadPool(messageProducers.size());
        messageProducers.forEach(messageProducer -> executorService.execute(() -> messageProducer.run()));
        executorService.shutdown();
    }

}
