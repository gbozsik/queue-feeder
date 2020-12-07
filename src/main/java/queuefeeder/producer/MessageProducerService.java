package queuefeeder.producer;

import queuefeeder.FeedingParams;

import java.util.List;

public interface MessageProducerService {

    List<Character> getPrefixesCharList(int numberOfThreads);

    List<MessageProducer> getMessageProducers(List<Character> prefixes, FeedingParams feedingParams);

    void startProducerThreads(List<MessageProducer> messageProducers);
}
