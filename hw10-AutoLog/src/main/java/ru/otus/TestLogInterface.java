package ru.otus;

import ru.otus.annotations.Log;

public interface TestLogInterface {

     @Log
     void calculation(int param);

     @Log
     void calculation(int param, int param2);

     void calculation(int param, int param2, int param3);
}
