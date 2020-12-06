package queuefeeder;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Builder
public class FeedingParams {

    private int numberOfMessageProducer;
    private int numberOfMessageProcessor;
    private int messagesPerMessageType;
    private String poisonPill;
    private ArrayBlockingQueue<String> arrayBlockingQueue;
    private LinkedBlockingQueue<String> linkedBlockingQueue;
}
