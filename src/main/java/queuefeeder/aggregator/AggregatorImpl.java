package queuefeeder.aggregator;

import java.util.concurrent.LinkedBlockingQueue;

public class AggregatorImpl<E> implements Aggregator<E> {

    private final LinkedBlockingQueue<E> linkedBlockingQueue;

    public AggregatorImpl(LinkedBlockingQueue<E> linkedBlockingQueue) {
        this.linkedBlockingQueue = linkedBlockingQueue;
    }

    @Override
    public void aggregate(E message) {
        linkedBlockingQueue.add(message);
    }
}
