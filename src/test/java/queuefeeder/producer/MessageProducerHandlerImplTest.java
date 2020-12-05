package queuefeeder.producer;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;

public class MessageProducerHandlerImplTest {

    int numberOfThreads = 5;
    ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(7);
    MessageProducerHandlerImpl messageProducerHandlerImpl = new MessageProducerHandlerImpl(numberOfThreads, arrayBlockingQueue, 5, "poisonPill");

    @Test
    public void getPrefixList() {
        List<Character> prefixes = messageProducerHandlerImpl.getPrefixList(numberOfThreads);

        assertEquals(numberOfThreads, prefixes.size());
        assertEquals(Character.valueOf('A'), prefixes.get(0));
        assertEquals(Character.valueOf('B'), prefixes.get(1));
        assertEquals(Character.valueOf('C'), prefixes.get(2));
        assertEquals(Character.valueOf('D'), prefixes.get(3));
        assertEquals(Character.valueOf('E'), prefixes.get(4));
    }

    @Test
    public void getPrefixList_zero_thread() {
        List<Character> prefixes = messageProducerHandlerImpl.getPrefixList(0);

        assertEquals(0, prefixes.size());
    }

}