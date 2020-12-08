package queuefeeder.producer.service;

import queuefeeder.producer.producer.MessageProducer;
import queuefeeder.producer.producer.MessageProducerImpl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageProducerExecutorService<T> {

    protected void startProcessorsOnDifferentThreads(List<MessageProducer<T>> messageProducers) {
        ExecutorService executorService = Executors.newFixedThreadPool(messageProducers.size());
        messageProducers.forEach(executorService::execute);
        executorService.shutdown();
    }

}
