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

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
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
            throw new IllegalArgumentException(String.format("Given class is not ru.otus.config %s", configClass.getName()));
        }
    }

    private Object createConfigInstants(Class<?> configClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            Constructor<?> constructor = configClass.getConstructor();
            return constructor.newInstance();
    }

    private Map<Integer, List<Method>> getSortedMethods(Method[] methods) {

        Map<Integer, List<Method>> methodsListSortedByOrder = new HashMap<>();

        for (Method method : methods) {
            var annotationOrder = method.getAnnotation(AppComponent.class).order();
            if (!methodsListSortedByOrder.containsKey(annotationOrder)) {
                List<Method> newMethodsList = new ArrayList<>();
                newMethodsList.add(method);
                methodsListSortedByOrder.put(annotationOrder, newMethodsList);
            } else {
                List<Method> currentMethodsList = methodsListSortedByOrder.get(annotationOrder);
                currentMethodsList.add(method);
                methodsListSortedByOrder.replace(annotationOrder, currentMethodsList);
            }
        }
        return methodsListSortedByOrder;
    }

    private void invokeMethods(Map<Integer, List<Method>> sortedMethods, Object obj) throws InvocationTargetException, IllegalAccessException {

        for (Integer i : sortedMethods.keySet()) {
            List<Method> currentMethods = sortedMethods.get(i);
            for (Method method : currentMethods) {
                invokeMethod(method, obj);
            }
        }
    }

    private void invokeMethod(Method method, Object obj) throws InvocationTargetException, IllegalAccessException {

        var annotationName = method.getAnnotation(AppComponent.class).name();
        var methodParameters = method.getParameterTypes();
        Object[] objects = new Object[methodParameters.length];

            for (int i = 0; i < methodParameters.length; i++) {
                var currentType = methodParameters[i];
                objects[i] = getAppComponent(currentType);
            }

            Object objCurr = method.invoke(obj, objects);
            appComponentsByName.put(annotationName, objCurr);
            appComponents.add(objCurr);
    }
}
