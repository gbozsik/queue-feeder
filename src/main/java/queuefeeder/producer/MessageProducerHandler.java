package queuefeeder.producer;

import java.util.List;

public interface MessageProducerHandler {

    void produceMessages();

    List<Character> getPrefixes();
}
