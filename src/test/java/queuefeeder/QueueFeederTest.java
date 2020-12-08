package queuefeeder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import queuefeeder.dispatcher.DispatcherService;
import queuefeeder.dispatcher.DispatcherServiceImpl;
import queuefeeder.processor.service.MessageConsumerService;
import queuefeeder.processor.service.MessageConsumerServiceImpl;
import queuefeeder.producer.service.MessageProducerService;
import queuefeeder.producer.service.MessageProducerServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueueFeederTest {

    private static final int numberOfMessageProducer = 7;
    private static final int numberOfMessageProcessor = 3;
    private static final int messagesPerMessageType = 5;
    private static final int arrayBlockingQueueCapacity = 7;
    private static final String poisonPill = "stop";
    ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(arrayBlockingQueueCapacity);
    LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();

    FeedingParams feedingParams = FeedingParams.builder()
            .numberOfMessageProducer(numberOfMessageProducer)
            .numberOfMessageProcessor(numberOfMessageProcessor)
            .messagesPerMessageType(messagesPerMessageType)
            .poisonPill(poisonPill)
            .arrayBlockingQueue(arrayBlockingQueue)
            .linkedBlockingQueue(linkedBlockingQueue)
            .build();

    @Mock
    private final DispatcherService dispatcherService = new DispatcherServiceImpl();
    @Mock
    private final MessageProducerService messageProducerService = new MessageProducerServiceImpl();
    @Mock
    private final MessageConsumerService messageConsumerService = new MessageConsumerServiceImpl();
    @InjectMocks
    private final QueueFeeder queueFeeder = new QueueFeeder();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void feed() {
        List<Character> characters = List.of('A', 'B');
        when(messageProducerService.getPrefixesCharList(anyInt())).thenReturn(characters);
        ArgumentCaptor<List<Character>> listArgumentCaptor = ArgumentCaptor.forClass(ArrayList.class);

//        queueFeeder.feed(feedingParams);

        verify(messageProducerService).getPrefixesCharList(anyInt());
        verify(messageProducerService).getMessageProducers(listArgumentCaptor.capture(), any(FeedingParams.class));
        assertEquals(2, listArgumentCaptor.getValue().size());
    }

    @Spy
    MessageProducerService spyMessageProducerService = new MessageProducerServiceImpl();

    @Test
    public void feed_spy() {
        final DispatcherService dispatcherService = new DispatcherServiceImpl();
        final MessageConsumerService messageConsumerService = new MessageConsumerServiceImpl();
        final QueueFeeder queueFeeder = new QueueFeeder(spyMessageProducerService, dispatcherService, messageConsumerService);

        List<Character> characters = List.of('A', 'B');
        when(messageProducerService.getPrefixesCharList(anyInt())).thenReturn(characters);

        queueFeeder.feed(feedingParams);

        verify(spyMessageProducerService).getMessageProducers(anyList(), any());
    }
}