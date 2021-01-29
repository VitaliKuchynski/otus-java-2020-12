package ru.otus.reflection;

import ru.otus.reflection.annotations.After;
import ru.otus.reflection.annotations.Before;
import ru.otus.reflection.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {

        new TestRunner(ClassTest.class).runTests();

    }

    private Class<?> clazzTest;

    public TestRunner(Class<?> type) {
        this.clazzTest = type;
    }

    public void runTests(){

        int passedTest = 0;
        int failedTest = 0;

        List<Method> beforeList = new ArrayList<>();
        List<Method> testList = new ArrayList<>();
        List<Method> afterList = new ArrayList<>();

        Method[] annotatedMethod = clazzTest.getDeclaredMethods();

        for (Method method: annotatedMethod) {

            if (method.getAnnotation(Before.class) != null) {
                beforeList.add(method);
            } else if (method.getAnnotation(Test.class) != null) {
                testList.add(method);
            } else if (method.getAnnotation(After.class) != null) {
                afterList.add(method);
            }
        }

        for (Method methodsBefore: beforeList) {
            try {
                methodsBefore.invoke(new ClassTest());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Method methodsTest: testList) {
            try {
                methodsTest.invoke(new ClassTest());
                passedTest++;
            } catch (Exception e) {
                failedTest++;
                e.printStackTrace();
            }
        }

        for (Method methodsAfter: afterList) {
            try {
                methodsAfter.invoke(new ClassTest());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Number of passed tests: " + passedTest);
        System.out.println("Number of failed tests: " + failedTest);
        System.out.println("Total number of failed tests: " + (passedTest + failedTest));
    }
}
