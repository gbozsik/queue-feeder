package queuefeeder.processor;

import lombok.Getter;
import queuefeeder.aggregator.Aggregator;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageProcessor {

    private final List<Character> prefixes;
    private final Aggregator<String> aggregator;

    @Getter
    private int processedMessageCount;

    public MessageProcessor(List<Character> charactersForAProcess, Aggregator<String> aggregator) {
      this.prefixes = charactersForAProcess;
      this.aggregator = aggregator;
    }

    public boolean accept(String value) {
        return prefixes.contains(value.charAt(0));
    }

    public void process(String value) {
        String processedValue = value.concat("-processed");
        aggregator.aggregate(processedValue);
        processedMessageCount++;
    }

    public String getPrefixesInString() {
        StringBuilder stringBuilder = new StringBuilder();
        prefixes.forEach(character -> stringBuilder.append(character).append(", "));
        return stringBuilder.toString();
    }
}
