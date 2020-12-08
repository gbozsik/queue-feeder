package queuefeeder.processor.consumer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import queuefeeder.processor.MessageProcessor;

import java.util.List;

@Slf4j
public class MessageConsumerImpl implements MessageConsumer<String> {

    private final List<Character> prefixes;
    private final MessageProcessor messageProcessor;
    @Getter
    private int consumeCount;
    @Setter
    private boolean consume = true;

    public MessageConsumerImpl(List<Character> prefixes, MessageProcessor messageProcessor) {
        this.prefixes = prefixes;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public boolean accept(String message) {
        return prefixes.contains(message.charAt(0));
    }

    @Override
    public void put(String message) {
        log.info("Thread of consumer: " + Thread.currentThread().getName());
        messageProcessor.process(message);
    }

//    @Override
//    public void setConsume(boolean consume) {
//        this.consume = consume;
//    }

    @Override
    public boolean consume() {
        return consume;
    }

    @Override
    public String getPrefixesInString() {
        StringBuilder stringBuilder = new StringBuilder();
        prefixes.forEach(character -> stringBuilder.append(character).append(", "));
        return stringBuilder.toString();
    }
}
