package queuefeeder.producer;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;

public class MessageProducerServiceImplTest {

    int numberOfThreads = 5;
    MessageProducerService messageProducerService = new MessageProducerServiceImpl();

    @Test
    public void getPrefixList() {
        List<Character> prefixes = messageProducerService.getPrefixesCharList(numberOfThreads);

        assertEquals(numberOfThreads, prefixes.size());
        assertEquals(Character.valueOf('A'), prefixes.get(0));
        assertEquals(Character.valueOf('B'), prefixes.get(1));
        assertEquals(Character.valueOf('C'), prefixes.get(2));
        assertEquals(Character.valueOf('D'), prefixes.get(3));
        assertEquals(Character.valueOf('E'), prefixes.get(4));
    }

    @Test
    public void getPrefixList_zero_thread() {
        List<Character> prefixes = messageProducerService.getPrefixesCharList(0);

        assertEquals(0, prefixes.size());
    }

}