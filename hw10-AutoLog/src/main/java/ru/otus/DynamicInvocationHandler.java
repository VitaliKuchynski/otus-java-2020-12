package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicInvocationHandler implements InvocationHandler {

    private final TestLogInterface testLogInterface;

    private Set<Method> methodList = selectAnnotatedMethods(TestLog.class, Log.class);

    public DynamicInvocationHandler(TestLogInterface testLog) {
        this.testLogInterface = testLog;
    }

    static TestLogInterface createMyClass() {
        InvocationHandler handler = new DynamicInvocationHandler(new TestLog());
        return (TestLogInterface) Proxy.newProxyInstance(DynamicInvocationHandler.class.getClassLoader(),
                new Class<?>[]{TestLogInterface.class}, handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for(Method me: methodList){
            if(method.getParameterCount() == me.getParameterCount() && me.getName().equals(method.getName())){
                System.out.println("Invoked method: " + method);
                return method.invoke(testLogInterface, args);
            }
        }
        return proxy;
    }

    private Set<Method> selectAnnotatedMethods(Class<?> clazzTest, Class annotation) {
        methodList = new HashSet<>();
        Method[] annotatedMethod = clazzTest.getDeclaredMethods();

        for (Method method : annotatedMethod) {

            if (method.getAnnotation(annotation) != null) {
                methodList.add(method);
            }
        }
        return methodList;
    }
}
