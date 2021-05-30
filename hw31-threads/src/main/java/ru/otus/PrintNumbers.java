package ru.otus;


public class PrintNumbers implements Runnable {

    public PrintNumbers(int[] num) {
        this.num = num;
    }

    private final int[] num;

    @Override
    public void run() {
        printArray();
    }

    private void printArray() {

        synchronized (this) {
            for (int i = 0; i < num.length; i++) {
                notify();
                System.out.println(Thread.currentThread().getName() + " # " + num[i]);
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = num.length - 1; i >= 0; i--) {
                notify();
                System.out.println(Thread.currentThread().getName() + " # " + num[i]);
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}