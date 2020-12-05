package queuefeeder.processor;

import java.util.List;

public interface MessageProcessorProvider {

    List<MessageProcessor> getMessageProcessorList(int numberOfMessageProcessors, List<Character> messageTypes);
}
