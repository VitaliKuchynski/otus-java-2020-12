package ru.otus.reflection;

import ru.otus.reflection.annotations.After;
import ru.otus.reflection.annotations.Before;
import ru.otus.reflection.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private int passedTest = 0;
    private int failedTest = 0;

    private final List<Method> beforeList = new ArrayList<>();
    private final List<Method> testList = new ArrayList<>();
    private final List<Method> afterList = new ArrayList<>();

    public void runTestClass(Class<?> clazzTest) {
        selectAnnotations(clazzTest);
        executeTests(clazzTest);
        printTestResults();
    }

    private void executeTests(Class<?> clazzTest) {

        for (Method methodsTest : testList) {

            Object testObj = null;

            try {
                testObj = clazzTest.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            for (Method methodsBefore : beforeList) {

                try {
                    methodsBefore.invoke(testObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                try {
                    methodsTest.invoke(testObj);
                    passedTest++;
                } catch (Exception e) {
                    failedTest++;
                    e.printStackTrace();
                }
            }

            for (Method methodsAfter : afterList) {
                try {
                    methodsAfter.invoke(testObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectAnnotations(Class<?> clazzTest) {

        Method[] annotatedMethod = clazzTest.getDeclaredMethods();

        for (Method method : annotatedMethod) {

            if (method.getAnnotation(Before.class) != null) {
                beforeList.add(method);
            } else if (method.getAnnotation(Test.class) != null) {
                testList.add(method);
            } else if (method.getAnnotation(After.class) != null) {
                afterList.add(method);
            }
        }
    }

    private void printTestResults() {
        System.out.println("Number of passed tests: " + passedTest);
        System.out.println("Number of failed tests: " + failedTest);
        System.out.println("Total number of tests: " + (passedTest + failedTest));
    }

}
