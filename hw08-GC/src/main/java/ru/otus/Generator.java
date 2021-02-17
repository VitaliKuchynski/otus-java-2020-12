package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class Generator implements GeneratorMBean{

    public void generateOOM() throws InterruptedException {

        for (long i = 0; i < Long.MAX_VALUE; i++){
            List<Object> memoryFillIntVar = new ArrayList<>();
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                memoryFillIntVar.add(new Object());

                if (i % 2 == 0) {
                    memoryFillIntVar.remove(memoryFillIntVar.size() - 1);
                }
            }
            Thread.sleep(10);
        }
    }
}
