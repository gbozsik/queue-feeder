package queuefeeder.processor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import queuefeeder.processor.service.MessageConsumerServiceImpl;

import java.util.List;

import static org.junit.Assert.*;

public class MessageConsumerServiceImplTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private MessageConsumerServiceImpl messageProcessorProvider = new MessageConsumerServiceImpl();

    @Test
    public void getMessageProcessorList() {
        List<Character> messageTypes = List.of('A', 'B', 'C', 'D', 'E');
        List<MessageProcessor> messageProcessors = messageProcessorProvider.getMessageConsumerExecutorList(2);

        assertEquals(2, messageProcessors.size());
        assertEquals("A, B, ", messageProcessors.get(0).getPrefixesInString());
        assertEquals("C, D, E, ", messageProcessors.get(1).getPrefixesInString());
    }

    @Test
    public void getMessageTypeQuantityPerProcessList() {
        List<Integer> messageTypeQuantityPerProcessList = messageProcessorProvider.getMessageTypeQuantityPerProcessList(10, 3);

        assertEquals(Integer.valueOf(3), messageTypeQuantityPerProcessList.get(0));
        assertEquals(Integer.valueOf(3), messageTypeQuantityPerProcessList.get(1));
        assertEquals(Integer.valueOf(4), messageTypeQuantityPerProcessList.get(2));
    }

    @Test
    public void getMessageTypeQuantityPerProcessList_messageTypeQuantity_zero() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("No produced message");
        messageProcessorProvider.getMessageTypeQuantityPerProcessList(0, 3);
    }

    @Test
    public void getMessageTypeQuantityPerProcessList_numberProcesses_zero() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Number of processors can not be 0");
        messageProcessorProvider.getMessageTypeQuantityPerProcessList(4, 0);
    }
}