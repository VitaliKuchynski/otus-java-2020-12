package ru.otus;


public class TestLog implements TestLogInterface {

    @Override
    public void calculation(int param) {
        System.out.println("calculation, param: " + param);
    }

    @Override
    public void calculation(int param, int param2) {
        System.out.println("calculation, param: " + (param + param2));
    }

    @Override
    public void calculation(int param, int param2, int param3) {
        System.out.println("calculation, param: " + (param + param2 + param3));
    }
}
