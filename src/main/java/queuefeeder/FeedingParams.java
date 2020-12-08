package queuefeeder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import queuefeeder.aggregator.Aggregator;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class FeedingParams<T> {

    private int numberOfMessageProducer;
    private int numberOfMessageProcessor;
    private int messagesPerMessageType;
    private T poisonPill;
    private List<Character> messageTypes;
    private ArrayBlockingQueue<T> arrayBlockingQueue;
    private LinkedBlockingQueue<T> linkedBlockingQueue;
    private Aggregator<T> aggregator;
}
