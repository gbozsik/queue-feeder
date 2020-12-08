package queuefeeder.producer.generator;

public class MessageGeneratorImpl implements MessageGenerator<String> {

    private final char prefix;
    private  int suffix;

    public MessageGeneratorImpl(char prefix) {
        this.prefix = prefix;
    }

    public String getMessage() {
        return String.format("%s%s", prefix, ++suffix);
    }
}
