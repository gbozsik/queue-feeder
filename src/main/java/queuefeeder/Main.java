package queuefeeder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

//    private static final int NUMBER_OF_MESSAGE_PRODUCER = 7;
//    private static final int NUMBER_OF_MESSAGE_PROCESSOR = 3;
//    private static final int MESSAGES_PER_MESSAGE_TYPE = 5;
//    private static final int ARRAY_BLOCKING_QUEUE_CAPACITY = 7;
//    //    private static final String POISON_PILL = "stop";
//    ArrayBlockingQueue<T> arrayBlockingQueue = new ArrayBlockingQueue<>(ARRAY_BLOCKING_QUEUE_CAPACITY);
//    LinkedBlockingQueue<T> linkedBlockingQueue = new LinkedBlockingQueue<>();
//    Aggregator<T> aggregator = Aggregator.getAggregator();

    public static void main(String[] args) {
        //        MessageProducerService<String> messageProducerService = MessageProducerService.getMessageProducerService();
//        DispatcherService dispatcherService = new DispatcherServiceImpl();
//        MessageConsumerService<String> messageConsumerService = new MessageConsumerServiceImpl();

//        FeedingParams<String> feedingParams = FeedingParams.builder()
//                .numberOfMessageProducer(NUMBER_OF_MESSAGE_PRODUCER)
//                .numberOfMessageProcessor(NUMBER_OF_MESSAGE_PROCESSOR)
//                .messagesPerMessageType(MESSAGES_PER_MESSAGE_TYPE)
//                .poisonPill(POISON_PILL)
//                .arrayBlockingQueue(arrayBlockingQueue)
//                .linkedBlockingQueue(linkedBlockingQueue)
//                .aggregator(aggregator)
//                .build();
        new QueueFeeder().feed();

    }
}
