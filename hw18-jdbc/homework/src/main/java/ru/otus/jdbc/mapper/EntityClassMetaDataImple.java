package ru.otus.jdbc.mapper;

import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EntityClassMetaDataImple implements EntityClassMetaData {

    private Class<?> clazz;

    public EntityClassMetaDataImple(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public <T> Constructor<T> getConstructor() {

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<T> constructor1 = null;

        for (Constructor constructor : constructors) {

            Class<?>[] pType = constructor.getParameterTypes();

            if (pType.length == 0) {
                return constructor;
            }
            constructor1 = constructor;
        }
        return constructor1;
    }

    @Override
    public Field getIdField() {

        for (Field field : getAllFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }

        try {
            return clazz.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {

        List<Field> fieldList = new ArrayList<>();

        for (Field field : getAllFields()) {

            if (!field.getName().equalsIgnoreCase("id") && !field.isAnnotationPresent(Id.class)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
