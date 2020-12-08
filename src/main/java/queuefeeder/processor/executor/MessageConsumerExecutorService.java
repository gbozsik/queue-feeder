package queuefeeder.processor.executor;


import queuefeeder.processor.executor.MessageConsumerExecutor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MessageConsumerExecutorService<T> {

//    public void startProcessorsOnDifferentThreads(List<MessageConsumerExecutor> messageProcessors) {
//        ExecutorService executorService = Executors.newFixedThreadPool(messageProcessors.size());
//        messageProcessors.forEach(messageProcessor -> {
//            System.out.println("Processor " + messageProcessor.getPrefixesInString() + " started");
//            Future<MessageProcessor> messageProcessorFuture = executorService.submit(() -> messageProcessor);
//        });
//        executorService.shutdown();
//
//    }

    public void startMessageConsumerExecutorsOnDifferentThreads(List<MessageConsumerExecutor<T>> messageConsumerExecutors) {
        ExecutorService executorService = Executors.newFixedThreadPool(messageConsumerExecutors.size());
        messageConsumerExecutors.forEach(messageConsumerExecutor -> {
            executorService.execute(messageConsumerExecutor);
            System.out.println("Processor " + messageConsumerExecutor.getMessageConsumer().getPrefixesInString() + " started");
        });
        executorService.shutdown();

    }
}
