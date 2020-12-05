package queuefeeder.dispatcher;

import queuefeeder.processor.MessageProcessor;

import java.util.List;

public interface Dispatcher {

    void dispatch(String poisonPill, int numberOfMessageProducer, List<MessageProcessor> messageProcessors);

    void dumpResults();
}
