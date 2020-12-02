package queuefeeder.processor;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageProcessor {

    private final List<Character> prefixes;
    @Getter
    private int processedMessageCount;

    public MessageProcessor(List<Character> prefixes) {
      this.prefixes = prefixes;
    }

    public boolean accept(String value) {
        return prefixes.contains(value.charAt(0));
    }

    public void process(String value, LinkedBlockingQueue<String> stringLinkedBlockingQueue) {
        String processedValue = value.concat("-processed");
        stringLinkedBlockingQueue.add(processedValue);
        processedMessageCount++;
    }

    public String getPrefixesInString() {
        StringBuilder stringBuilder = new StringBuilder();
        prefixes.forEach(character -> stringBuilder.append(character + ", "));
        return stringBuilder.toString();
    }
}
