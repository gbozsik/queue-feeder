package queuefeeder.producer.generator;

public interface MessageGenerator<T> {

    static MessageGenerator<String> getStringMessageGenerator(char prefix) {
        return new MessageGeneratorImpl(prefix);
    }

    T getMessage();
}
