package queuefeeder.producer.service;

import lombok.Getter;
import queuefeeder.FeedingParams;
import queuefeeder.producer.producer.MessageProducer;
import queuefeeder.producer.producer.MessageProducerImpl;

import java.util.ArrayList;
import java.util.List;

public class MessageProducerServiceImpl implements MessageProducerService<String> {

    @Getter
    String poisonPill;

    public MessageProducerServiceImpl(String poisonPill) {
        this.poisonPill = poisonPill;
    }

    @Override
    public List<Character> getPrefixesCharList(int numberOfThreads) {
        List<Character> prefixes = new ArrayList<>();
        if (numberOfThreads > 26) {
            throw new IllegalArgumentException("Threads maximum number is 26");
        }
        for (int prefixCharPosition = 0; prefixCharPosition < numberOfThreads; prefixCharPosition++) {
            char prefix = (char) (prefixCharPosition + 65);
            prefixes.add(prefix);
        }
        return prefixes;
    }

    @Override
    public List<MessageProducer<String>> getMessageProducers(List<Character> prefixes, FeedingParams<String> feedingParams) {
        feedingParams.setPoisonPill(getPoisonPill());
        List<MessageProducer<String>> messageProducers = new ArrayList<>();
        for (Character prefix : prefixes) {
            MessageProducer<String> stringMessageProducer = MessageProducer.getStringMessageProducer(prefix, feedingParams.getArrayBlockingQueue(),
                    feedingParams.getMessagesPerMessageType(), getPoisonPill());

            messageProducers.add(stringMessageProducer);
        }
        return messageProducers;
    }

    @Override
    public void startProducerThreads(List<MessageProducer<String>> messageProducers) {
        new MessageProducerExecutorService<String>().startProcessorsOnDifferentThreads(messageProducers);
    }

}
