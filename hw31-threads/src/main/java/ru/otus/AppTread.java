package ru.otus;

public class AppTread {

    public static void main(String[] args) {

        PrintNumbers t = new PrintNumbers(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);

        t1.setName("Thread 1");
        t2.setName("Thread 2");

        t1.start();
        t2.start();
    }
}



