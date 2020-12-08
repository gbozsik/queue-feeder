package queuefeeder.dispatcher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherExecutorService<T> {

    public static void startDispatcherExecutorsOnDifferentThreads(List<DispatcherExecutor<String>> dispatcherExecutors) {
        ExecutorService dispatcherExecutorService = Executors.newFixedThreadPool(dispatcherExecutors.size());
        for (DispatcherExecutor<String> dispatcherExecutor : dispatcherExecutors) {
            dispatcherExecutorService.execute(dispatcherExecutor);
        }
        dispatcherExecutorService.shutdown();
    }
}
