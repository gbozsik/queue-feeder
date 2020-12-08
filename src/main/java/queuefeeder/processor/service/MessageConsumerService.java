package queuefeeder.processor.service;

import queuefeeder.FeedingParams;
import queuefeeder.processor.executor.MessageConsumerExecutor;

import java.util.List;

public interface MessageConsumerService<T> {

    static MessageConsumerService<String> getStringMessageConsumerService() {
        return new MessageConsumerServiceImpl();
    }

    List<MessageConsumerExecutor<T>> getMessageConsumerExecutorList(FeedingParams<T> feedingParams);
}
