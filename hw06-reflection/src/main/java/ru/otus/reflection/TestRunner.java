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

    public void runTestClass(Class<?> clazzTest) {
        executeTests(clazzTest);
        printTestResults();
    }

    private void executeTests(Class<?> clazzTest) {

        for (Method methodsTest : selectAnnotatedMethods(clazzTest, Test.class)) {

            Object ob = createNewInstance(clazzTest);
            boolean failed = callBeforeMethods(clazzTest, ob);

            if (!failed) {
                if (callMethod(methodsTest, ob)) {
                    passedTest++;
                } else {
                    failedTest++;
                }
            }

            callAfterMethods(clazzTest, ob);
        }
    }

    private void callAfterMethods(Class<?> clazz, Object ob) {

        for (Method methodsAfter : selectAnnotatedMethods(clazz, After.class)) {
            callMethod(methodsAfter, ob);
        }
    }

    private boolean callBeforeMethods(Class<?> clazz, Object ob) {

        for (Method methodsAfter : selectAnnotatedMethods(clazz, Before.class)) {
            if (!callMethod(methodsAfter, ob)) {
                return true;
            }
        }
        return false;
    }

    private boolean callMethod(Method method, Object obj) {
        try {
            method.invoke(obj);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Object createNewInstance(Class<?> clazzTest) {
        Object testObj = null;

        try {
            testObj = clazzTest.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return testObj;
    }

    private List<Method> selectAnnotatedMethods(Class<?> clazzTest, Class annotation) {
        List<Method> methodList = new ArrayList<>();
        Method[] annotatedMethod = clazzTest.getDeclaredMethods();

        for (Method method : annotatedMethod) {

            if (method.getAnnotation(annotation) != null) {
                methodList.add(method);
            }

        }
        return methodList;
    }

    private void printTestResults() {
        System.out.println("Number of passed tests: " + passedTest);
        System.out.println("Number of failed tests: " + failedTest);
        System.out.println("Total number of tests: " + (passedTest + failedTest));
    }

}
