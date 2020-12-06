package queuefeeder.producer;

import queuefeeder.FeedingParams;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public interface MessageProducerHandler {

    List<Character> getPrefixesCharList(int numberOfThreads);

    List<MessageProducer> getMessageProducers(List<Character> prefixes, FeedingParams feedingParams);

    void startProducerThreads(List<MessageProducer> messageProducers);
}
