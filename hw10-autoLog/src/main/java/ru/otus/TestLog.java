package ru.otus;

import ru.otus.annotations.Log;

public class TestLog implements TestLogInterface {

    @Override
    public void calculation(int param) {
        System.out.println("calculation, param: " + param);
    }

    @Log
    @Override
    public void calculation(int param, int param2) {
        System.out.println("calculation, param: " + (param + param2));
    }

    @Log
    @Override
    public void calculation(int param, int param2, int param3) {
        System.out.println("calculation, param: " + (param + param2 + param3));
    }
}
