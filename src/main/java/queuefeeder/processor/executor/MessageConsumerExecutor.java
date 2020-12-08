package queuefeeder.processor.executor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import queuefeeder.processor.consumer.MessageConsumer;

@Slf4j
public class MessageConsumerExecutor<T> implements Runnable {

    @Getter
    private final MessageConsumer<T> messageConsumer;

    public MessageConsumerExecutor(MessageConsumer<T> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void run() {
        log.info("Start execution of MessageConsumerExecutor.");
        boolean messageToConsumeExist = true;
        while (messageToConsumeExist) {
            messageToConsumeExist = messageConsumer.consume();
        }
    }
}
