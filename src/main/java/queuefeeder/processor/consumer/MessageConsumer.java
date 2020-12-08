package queuefeeder.processor.consumer;

import queuefeeder.processor.MessageProcessor;

import java.util.List;

public interface MessageConsumer<T> {

    static MessageConsumer<String> getMessageConsumer(List<Character> charactersForAProcess, MessageProcessor messageProcessor) {
        return new MessageConsumerImpl(charactersForAProcess, messageProcessor);
    }

    boolean accept(T message);

    void put(T message);

    void setConsume(boolean consume);

    boolean consume();

    String getPrefixesInString();

    int getConsumeCount();
}
