package queuefeeder.processor.service;

import queuefeeder.FeedingParams;
import queuefeeder.processor.MessageProcessor;
import queuefeeder.processor.consumer.MessageConsumer;
import queuefeeder.processor.executor.MessageConsumerExecutor;
import queuefeeder.processor.executor.MessageConsumerExecutorService;

import java.util.ArrayList;
import java.util.List;

public class MessageConsumerServiceImpl implements MessageConsumerService<String> {

    public List<MessageConsumerExecutor<String>> getMessageConsumerExecutorList(FeedingParams<String> feedingParams) {
        List<Integer> messageTypeQuantityPerProcessList =
                getMessageTypeQuantityPerProcessList(feedingParams.getMessageTypes().size(), feedingParams.getNumberOfMessageProcessor());

        List<MessageConsumerExecutor<String>> messageConsumerExecutors =
                getMessageConsumerExecutors(messageTypeQuantityPerProcessList, feedingParams);
        new MessageConsumerExecutorService<String>().startMessageConsumerExecutorsOnDifferentThreads(messageConsumerExecutors);
        return messageConsumerExecutors;
    }

    List<Integer> getMessageTypeQuantityPerProcessList(int messageTypesQuantity, int numberProcesses) {
        List<Integer> messageTypeQuantityPerProcessList = new ArrayList<>();

        if (messageTypesQuantity == 0) {
            throw new IllegalArgumentException("No produced message");
        }
        if (numberProcesses == 0) {
            throw new IllegalArgumentException("Number of processors can not be 0");
        }
        int threadPerProcess = messageTypesQuantity / numberProcesses;
        int startedThreads = 0;
        for (int i = 0; i < numberProcesses; i++) {
            int tasksPerProcess;
            if (i != numberProcesses - 1) {
                tasksPerProcess = threadPerProcess;
                startedThreads += threadPerProcess;
                messageTypeQuantityPerProcessList.add(tasksPerProcess);
                continue;
            }
            tasksPerProcess = messageTypesQuantity - startedThreads;
            messageTypeQuantityPerProcessList.add(tasksPerProcess);
        }
        return messageTypeQuantityPerProcessList;
    }

//    private List<MessageProcessor> getMessageProcessors(List<Integer> messageTypeQuantityPerProcessList, List<Character> messagePrefixes) {
//        List<MessageProcessor> messageProcessors = new ArrayList<>();
//        int startedTasksNumber = 0;
//        for (int i = 0; i < messageTypeQuantityPerProcessList.size(); i++) {
//            int messageTypeQuantityForProcess = messageTypeQuantityPerProcessList.get(i);
//            int lastCharacterPosition = startedTasksNumber + messageTypeQuantityForProcess;
//            List<Character> charactersForAProcess = messagePrefixes.subList(startedTasksNumber, lastCharacterPosition);
//            messageProcessors.add(new MessageProcessor(charactersForAProcess));
//            startedTasksNumber += messageTypeQuantityForProcess;
//        }
//        return messageProcessors;
//    }

    private  List<MessageConsumerExecutor<String>> getMessageConsumerExecutors(List<Integer> messageTypeQuantityPerProcessList, FeedingParams<String> feedingParams) {
        List<MessageConsumerExecutor<String>> messageConsumers = new ArrayList<>();
        int startedTasksNumber = 0;
        for (int i = 0; i < messageTypeQuantityPerProcessList.size(); i++) {
            int messageTypeQuantityForProcess = messageTypeQuantityPerProcessList.get(i);
            int lastCharacterPosition = startedTasksNumber + messageTypeQuantityForProcess;

            List<Character> charactersForAProcess = feedingParams.getMessageTypes().subList(startedTasksNumber, lastCharacterPosition);
            MessageProcessor messageProcessor = new MessageProcessor(charactersForAProcess, feedingParams.getAggregator());
            MessageConsumer<String> messageConsumer = MessageConsumer.getMessageConsumer(charactersForAProcess, messageProcessor);
            messageConsumers.add(new MessageConsumerExecutor<>(messageConsumer));
            startedTasksNumber += messageTypeQuantityForProcess;
        }
        return messageConsumers;
    }
}
