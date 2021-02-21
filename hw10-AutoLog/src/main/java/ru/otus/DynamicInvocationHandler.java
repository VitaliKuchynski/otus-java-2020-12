package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicInvocationHandler implements InvocationHandler {

    private final TestLogInterface testLogInterface;

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

        if(method.isAnnotationPresent(Log.class)){
            System.out.println("Invoked method: " + method);
            return method.invoke(testLogInterface, args);
        }
        System.out.println( "Unsupported method: " + method.getName() + " parameter counts: " + method.getParameterCount());
        return proxy;
    }
}
