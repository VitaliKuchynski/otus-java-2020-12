package ru.otus;

public class AppThreadRun {

    public static void main(String[] args) {

        final Object mutex = new Object();
        final boolean[] flag = {false};

        Thread t1 = new Thread(new Runnable() {
            int counter = 0;

            @Override
            public void run() {

                synchronized (mutex) {

                    for(int i = 0; i <= 20; i++) {

                            while(flag[0]) {
                                try {
                                    mutex.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        if(i < 10) {
                            System.out.println("Tread 1 # " + counter);
                            counter++;
                        }
                        if(i >= 10) {
                            System.out.println("Tread 1 # " + counter);
                            counter--;
                        }
                        flag[0] = true;
                        mutex.notify();
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            int counter = 0;

            @Override
            public void run() {

                synchronized (mutex) {
                    for(int i = 0; i <= 20; i++){

                        try {
                            while(!flag[0]) {
                                mutex.wait();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(i < 10) {
                            System.out.println("Tread 2 # " + counter);
                            counter++;
                        }
                        if(i >= 10) {
                            System.out.println("Tread 2 # " + counter);
                            counter--;
                        }
                        flag[0] = false;
                        mutex.notify();

                    }
                }
            }
        });

        t2.start();
        t1.start();
    }
}
