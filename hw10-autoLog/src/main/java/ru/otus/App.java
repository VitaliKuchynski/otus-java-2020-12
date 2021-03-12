package ru.otus;

public class App {

    public static void main(String[] args) {
        TestLogInterface testLogInterface = DynamicInvocationHandler.createMyClass();
        testLogInterface.calculation(6, 3);
        testLogInterface.calculation(3);
        testLogInterface.calculation(6, 3, 3);
    }
}
