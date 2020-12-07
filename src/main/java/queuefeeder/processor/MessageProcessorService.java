package queuefeeder.processor;

import java.util.List;

public interface MessageProcessorService {

    List<MessageProcessor> getMessageProcessorList(int numberOfMessageProcessors, List<Character> messageTypes);
}
