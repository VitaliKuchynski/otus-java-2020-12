package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class ProcessorEvenSecondsTracker implements Processor {

    @Override
    public Message process(Message message) {
      long currentLocalSecond = LocalDateTime.now().getSecond();
        if (currentLocalSecond % 2 == 0) {
            throw new RuntimeException(Long.toString(currentLocalSecond));
        }
        return message;
    }
}
