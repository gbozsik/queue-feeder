package queuefeeder.producer;

public class MessageGenerator {

    private final char prefix;

    public MessageGenerator(char prefix) {
        this.prefix = prefix;
    }

    public String getMessage(int suffix) {
        return String.format("%s%s", prefix, suffix);
    }
}
