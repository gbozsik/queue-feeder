package queuefeeder.dispatcher;

import queuefeeder.FeedingParams;
import queuefeeder.processor.MessageProcessor;

import java.util.List;

public interface Dispatcher {

    void dispatch(FeedingParams feedingParams, List<MessageProcessor> messageProcessors);
}
