package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws InvocationTargetException, IllegalAccessException {
        checkConfigClass(configClass);
        Object obj = createConfigInstants(configClass);
        Method[] methods = configClass.getDeclaredMethods();
        invokeMethods(getSortedMethods(methods), obj);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object component : appComponents) {
            if (componentClass.isInstance(component)) {
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

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object createConfigInstants(Class<?> configClass) {
        try {
            Constructor constructor = configClass.getConstructor();
            return constructor.newInstance(null);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return e;
        }
    }

    private Map<Integer, ArrayList<Method>> getSortedMethods(Method[] methods) {

        Map<Integer, ArrayList<Method>> methodsListSortedByOrder = new HashMap<>();

        for (Method method : methods) {
            var annotationOrder = method.getAnnotation(AppComponent.class).order();
            if (!methodsListSortedByOrder.containsKey(annotationOrder)) {
                ArrayList<Method> newMethodsList = new ArrayList<>();
                newMethodsList.add(method);
                methodsListSortedByOrder.put(annotationOrder, newMethodsList);
            } else {
                ArrayList<Method> currentMethodsList = methodsListSortedByOrder.get(annotationOrder);
                currentMethodsList.add(method);
                methodsListSortedByOrder.replace(annotationOrder, currentMethodsList);
            }
        }
        return methodsListSortedByOrder;
    }

    private void invokeMethods(Map<Integer, ArrayList<Method>> sortedMethods, Object obj) throws InvocationTargetException, IllegalAccessException {

        for (Integer i : sortedMethods.keySet()) {
            ArrayList<Method> currentMethods = sortedMethods.get(i);
            for (Method method : currentMethods) {
                invokeMethod(method, obj);
            }
        }
    }

    private void invokeMethod(Method method, Object obj) throws InvocationTargetException, IllegalAccessException {

        var annotationName = method.getAnnotation(AppComponent.class).name();
        var methodParameters = method.getParameterTypes();

        if (methodParameters.length == 0) {
            Object objCurr = method.invoke(obj);
            appComponentsByName.put(annotationName, objCurr);
            appComponents.add(objCurr);
        } else {
            Object[] objects = new Object[methodParameters.length];

            for (int i = 0; i < methodParameters.length; i++) {

                var currentType = methodParameters[i];

                for (Object o : appComponents) {
                    if (currentType.isInstance(o)) {
                        objects[i] = o;
                    }
                }
            }
            Object objCurr = method.invoke(obj, objects);
            appComponentsByName.put(annotationName, objCurr);
            appComponents.add(objCurr);
        }
    }
}
