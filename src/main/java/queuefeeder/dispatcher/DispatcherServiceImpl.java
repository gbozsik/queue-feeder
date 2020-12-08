package queuefeeder.dispatcher;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.FeedingParams;
import queuefeeder.processor.executor.MessageConsumerExecutor;

import java.util.List;
import java.util.Objects;

@Slf4j
public class DispatcherServiceImpl<T> implements DispatcherService<T> {

    private final FeedingParams<T> feedingParams;
    private final List<MessageConsumerExecutor<T>> messageConsumerExecutors;
    private boolean dipatching = true;

    DispatcherServiceImpl(FeedingParams<T> feedingParams, List<MessageConsumerExecutor<T>> messageConsumerExecutors) {
        this.feedingParams = feedingParams;
        this.messageConsumerExecutors = messageConsumerExecutors;
    }

    public boolean dispatching() {
        return dipatching;
    }

    @Override
    public boolean dispatch() {
        log.info("Thread of dispatcher: " + Thread.currentThread().getName());
        int countOfPoisonPill = 0;
        while (dipatching) {
            T messageFromBlockingQueue = feedingParams.getArrayBlockingQueue().poll();
            if (Objects.nonNull(messageFromBlockingQueue)) {
                if (messageFromBlockingQueue.equals(feedingParams.getPoisonPill())) {
                    countOfPoisonPill++;
                    if (countOfPoisonPill == feedingParams.getNumberOfMessageProducer()) {
                        messageConsumerExecutors.forEach(stringMessageConsumerExecutor ->
                                stringMessageConsumerExecutor.getMessageConsumer().setConsume(false));
                         return false;
                    }
                }
                for (MessageConsumerExecutor<T> messageConsumerExecutor : messageConsumerExecutors) {
                    if (messageConsumerExecutor.getMessageConsumer().accept(messageFromBlockingQueue)) {
                        messageConsumerExecutor.getMessageConsumer().consume();
                        messageConsumerExecutor.getMessageConsumer().put(messageFromBlockingQueue);
                        continue;
                    }
                }
            }
        }
        return true;
    }
}

