package queuefeeder.aggregator;

import java.util.concurrent.LinkedBlockingQueue;

public interface Aggregator<T> {

    static <T> Aggregator<T> getAggregator(LinkedBlockingQueue<T> linkedBlockingQueue) {
        return new AggregatorImpl<>(linkedBlockingQueue);
    }

    void aggregate(T message);
}
