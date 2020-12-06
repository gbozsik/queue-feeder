package queuefeeder.dispatcher;

import lombok.extern.slf4j.Slf4j;
import queuefeeder.FeedingParams;
import queuefeeder.processor.MessageProcessor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DispatcherImpl implements Dispatcher {

    public DispatcherImpl() {
    }

    @Override
    public void dispatch(FeedingParams feedingParams, List<MessageProcessor> messageProcessors) {
        int countOfPoisonPill = 0;
        while (true) {
            String messageFromBlockingQueue = feedingParams.getArrayBlockingQueue().poll();
            if (Objects.nonNull(messageFromBlockingQueue)) {
                if (messageFromBlockingQueue.equals(feedingParams.getPoisonPill())) {
                    countOfPoisonPill++;
                    if (countOfPoisonPill == feedingParams.getNumberOfMessageProducer() && feedingParams.getArrayBlockingQueue().isEmpty()) {
                        return;
                    }
                }
                for (MessageProcessor messageProcessor : messageProcessors) {
                    if (messageProcessor.accept(messageFromBlockingQueue)) {
                        messageProcessor.process(messageFromBlockingQueue, feedingParams.getLinkedBlockingQueue());
                    }
                }
            }
        }
    }
}

