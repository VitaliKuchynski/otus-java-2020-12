package ru.otus.reflection;

import ru.otus.reflection.annotations.After;
import ru.otus.reflection.annotations.Before;
import ru.otus.reflection.annotations.Test;

public class ClassTest {

    @Before
    public void setUp() {
        System.out.println("Set up precondition  ----- ");
    }

    @Test
    public void test1() {
        System.out.println("Run test 1 ----- ");
    }

    @Test
    public void test2() {
        throw new RuntimeException(" Test - 2  -----  Exception !!!!!!!!!!!!!!!!!!!!");
    }

    @Test
    public void test3() {
        System.out.println("Run test 3 ----- ");

    }

    @After
    public void tearDown() {
        System.out.println("Close test  ----- ");
    }

}
