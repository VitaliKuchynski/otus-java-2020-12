package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Method [] methods = configClass.getDeclaredMethods();

        for (Method method: methods ) {
          var annotationName = method.getAnnotation(AppComponent.class);
//           for (AnnotatedType s:  method.getAnnotatedParameterTypes()) {
//               // System.out.println(s.getType());
//            }
            //System.out.println(annotationName.name());


            var o = method.getReturnType();
            var interfaceClass = method.getGenericReturnType();
            appComponents.add(o);
            appComponents.add(interfaceClass);
            System.out.println(o.getSimpleName());
            System.out.println(interfaceClass);


        }

        // You code here...
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return null;
    }


    public static void main(String[] args) {
        AppComponentsContainerImpl appComponentsContainer = new AppComponentsContainerImpl(AppConfig.class);

    }
}
