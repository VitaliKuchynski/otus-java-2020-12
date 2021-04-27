package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Deque <Method> methodWithoutParam = new ArrayDeque<>();
    private final Deque <Method> methodsWithParam = new ArrayDeque<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object obj = createConfigInst(configClass);
        Method [] methods = configClass.getDeclaredMethods();
        sortMethodsByParameters(methods);
        invokeMethods(methodWithoutParam, obj);
        invokeMethods(methodsWithParam, obj);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        String str = componentClass.getSimpleName();
        for (Object component: appComponents) {
            if (component.getClass().getSimpleName().contains(str)) {
                return (C) component;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }
        throw new RuntimeException();
    }

    private void sortMethodsByParameters(Method [] methods) {
        for (Method method: methods) {
            Class<?>[] methodParameters = method.getParameterTypes();

            if (methodParameters.length == 0) {
                methodWithoutParam.push(method);
            } else {
                methodsWithParam.push(method);
            }
        }
    }

    private void invokeMethods(Deque<Method> methods, Object obj) {

        for (Method method : methods) {
            var annotationName = method.getAnnotation(AppComponent.class).name();
            var methodParameters = method.getParameterTypes();

            try {
                if (method.getParameterTypes().length == 0) {
                    Object objCurr = method.invoke(obj);
                    appComponentsByName.put(annotationName, objCurr);
                    appComponents.add(objCurr);

                } else if (methodParameters.length == 1) {
                    Object objCurr = null;
                    for (Object appComponent : appComponents) {
                        if (appComponent.getClass().getSimpleName().contains(methodParameters[0].getSimpleName())) {
                            objCurr = method.invoke(obj, appComponent);
                            appComponentsByName.put(annotationName, objCurr);
                        }
                    }
                    appComponents.add(objCurr);

                } else {
                    Object objCurr = method.invoke(obj, appComponents.get(0), appComponents.get(2), appComponents.get(1));
                    appComponentsByName.put(annotationName, objCurr);
                    appComponents.add(objCurr);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object createConfigInst(Class<?> configClass){
        try {
            Constructor constructor = configClass.getConstructor();
            return constructor.newInstance((Object[]) null);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return e;
        }
    }
}
