package queuefeeder.producer;

import lombok.Setter;

import javax.naming.MalformedLinkException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessageProducerExecutorService {

    protected void startProcessorsOnDifferentThreads(List<MessageProducer> messageProducers) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(messageProducers.size());
        messageProducers.forEach(messageProducer -> executorService.execute(() -> messageProducer.run()));
        executorService.shutdown();
    }

}
