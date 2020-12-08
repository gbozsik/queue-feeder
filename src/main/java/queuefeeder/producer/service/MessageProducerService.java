package queuefeeder.producer.service;

import queuefeeder.FeedingParams;
import queuefeeder.producer.producer.MessageProducer;
import queuefeeder.producer.producer.MessageProducerImpl;

import java.util.List;

public interface MessageProducerService<T> {

    static MessageProducerService<String> getStringMessageProducerService(String poisonPill) {
        return new MessageProducerServiceImpl(poisonPill);
    }

    T getPoisonPill();

    List<Character> getPrefixesCharList(int numberOfThreads);

    List<MessageProducer<T>> getMessageProducers(List<Character> prefixes, FeedingParams<T> feedingParams);

    void startProducerThreads(List<MessageProducer<T>> messageProducerImpls);
}
