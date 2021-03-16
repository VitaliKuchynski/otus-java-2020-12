package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinter;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.homework.ProcessorValueSwap;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {

        var processors = List.of(new ProcessorConcatFields(),
                new ProcessorValueSwap());

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
        var listenerPrinter = new ListenerPrinter();
        complexProcessor.addListener(listenerPrinter);

        var msg = new Message.Builder(1L)
                .field11("Test11")
                .field12("Test12")
                .build();

        var msg2 = new Message.Builder(1L)
                .field11("Test12")
                .field12("Test13")
                .build();

        var result = complexProcessor.handle(msg);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);

        var historyListener = new HistoryListener();
        historyListener.onUpdated(msg,msg2);
    }
}
