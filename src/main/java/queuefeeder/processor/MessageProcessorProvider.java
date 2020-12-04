package queuefeeder.processor;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessorProvider {

    public MessageProcessorProvider() {
    }

    public static List<MessageProcessor> getMessageProcessorList(int numberOfMessageProcessors, List<Character> messageTypes) {
        List<Integer> messageTypeQuantityPerProcessList = getMessageTypeQuantityPerProcessList(messageTypes.size(), numberOfMessageProcessors);
        List<MessageProcessor> messageProcessors = getMessageProcessors(messageTypeQuantityPerProcessList, messageTypes);
        new MessageProcessorExecutorService().startProcessorsOnDifferentThreads(messageProcessors);
        return messageProcessors;
    }

    static List<Integer> getMessageTypeQuantityPerProcessList(int messageTypesQuantity, int numberProcesses) {
        List<Integer> messageTypeQuantityPerProcessList = new ArrayList<>();

        if (messageTypesQuantity == 0) {
            throw new IllegalArgumentException("No produced message");
        }
        if (numberProcesses == 0) {
            throw new IllegalArgumentException("Number of processors can not be null");
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

    static List<MessageProcessor> getMessageProcessors(List<Integer> messageTypePerProcessNumberList, List<Character> messagePrefixes) {
        List<MessageProcessor> messageProcessors = new ArrayList<>();
        int startedTasksNumber = 0;
        for (int taskPerProcess = 0; taskPerProcess < messageTypePerProcessNumberList.size(); taskPerProcess++) {
            int messageTypeQuantityForProcess = messageTypePerProcessNumberList.get(taskPerProcess);
            int lastCharacterPosition = startedTasksNumber + messageTypeQuantityForProcess;
            List<Character> charactersForAProcess = messagePrefixes.subList(startedTasksNumber, lastCharacterPosition);
            messageProcessors.add(new MessageProcessor(charactersForAProcess));
            startedTasksNumber += messageTypeQuantityForProcess;
        }
        return messageProcessors;
    }

}
