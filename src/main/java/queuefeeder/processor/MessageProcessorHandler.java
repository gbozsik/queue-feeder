package queuefeeder.processor;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessorHandler {

    private final int numberOfMessageProcessors;
    private final List<Character> messageTypes;

    public MessageProcessorHandler(int numberOfMessageProcessors, List<Character> messageTypes) {
        this.numberOfMessageProcessors = numberOfMessageProcessors;
        this.messageTypes = messageTypes;
    }

    public List<MessageProcessor> getMessageProcessorList() {
        List<Integer> messageTypeQuantityPerProcessList = getMessageTypeQuantityPerProcessList(messageTypes.size(), numberOfMessageProcessors);
        List<MessageProcessor> messageProcessors = getMessageProcessors(messageTypeQuantityPerProcessList, messageTypes);
        new MessageProcessorExecutorService().startProcessorsOnDifferentThreads(messageProcessors);
        return messageProcessors;
    }

    private List<Integer> getMessageTypeQuantityPerProcessList(int messageType, int numberProcesses) {
        List<Integer> messageTypeQuantityPerProcessList = new ArrayList<>();

        if (messageType == 0) {
            throw new IllegalArgumentException("No produced message");
        }
        if (numberProcesses == 0) {
            throw new IllegalArgumentException("Number of processors can not be null");
        }
        int threadPerProcess = messageType / numberProcesses;
        int startedThreads = 0;
        for (int i = 0; i < numberProcesses; i++) {
            int tasksPerProcess;
            if (i != numberProcesses - 1) {
                tasksPerProcess = threadPerProcess;
                startedThreads += threadPerProcess;
                messageTypeQuantityPerProcessList.add(tasksPerProcess);
                continue;
            }
            tasksPerProcess = messageType - startedThreads;
            messageTypeQuantityPerProcessList.add(tasksPerProcess);
        }
        return messageTypeQuantityPerProcessList;
    }

    private List<MessageProcessor> getMessageProcessors(List<Integer> messageTypePerProcessNumberList, List<Character> messagePrefixes) {
        List<MessageProcessor> messageProcessors = new ArrayList<>();
        int startedTasksNumber = 0;
        for (int taskPerProcess = 0; taskPerProcess < messageTypePerProcessNumberList.size(); taskPerProcess++) {
            int lastCharacterPosition = startedTasksNumber + messageTypePerProcessNumberList.get(taskPerProcess);
            List<Character> charactersForAProcess = messagePrefixes.subList(startedTasksNumber, lastCharacterPosition);
            messageProcessors.add(new MessageProcessor(charactersForAProcess));
            startedTasksNumber += lastCharacterPosition;
        }
        return messageProcessors;
    }

}
