package queuefeeder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedingParams {

    private int numberOfMessageProducer;
    private int numberOfMessageProcessor;
    private int messagesPerMessageType;
    private int arrayBlockingQueueCapacity;
    private String poisonPill;
}
