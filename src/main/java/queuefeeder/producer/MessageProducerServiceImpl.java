package queuefeeder.producer;

import queuefeeder.FeedingParams;

import java.util.ArrayList;
import java.util.List;

public class MessageProducerServiceImpl implements MessageProducerService {

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
    public List<MessageProducer> getMessageProducers(List<Character> prefixes, FeedingParams feedingParams) {
        List<MessageProducer> messageProducers = new ArrayList<>();
        for (Character prefix : prefixes) {
            messageProducers.add(new MessageProducer(prefix, feedingParams.getArrayBlockingQueue(),
                    feedingParams.getMessagesPerMessageType(), feedingParams.getPoisonPill()));
        }
        return messageProducers;
    }

    @Override
    public void startProducerThreads(List<MessageProducer> messageProducers) {
        MessageProducerExecutorService messageProducerExecutorService = new MessageProducerExecutorService();
        messageProducerExecutorService.startProcessorsOnDifferentThreads(messageProducers);
    }

}
