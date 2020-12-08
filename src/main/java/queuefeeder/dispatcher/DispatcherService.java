package queuefeeder.dispatcher;

import queuefeeder.FeedingParams;
import queuefeeder.processor.executor.MessageConsumerExecutor;

import java.util.List;

public interface DispatcherService<T> {

//    static DispatcherService<String> getStringDispatcherService() {
//        return new DispatcherServiceImpl();
//    }

    static <T> DispatcherService<T> getDispatcherService(FeedingParams<T> feedingParams, List<MessageConsumerExecutor<T>> messageConsumerExecutors) {
        return new DispatcherServiceImpl<>(feedingParams, messageConsumerExecutors);
    }


    boolean dispatching();

    boolean dispatch();
}
