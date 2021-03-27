package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final Class<?> clazz;
    private final EntityClassMetaData entityClassMetaDataImple;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, Class<?> clazz) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.clazz = clazz;
        this.entityClassMetaDataImple = new EntityClassMetaDataImple(clazz);
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return resultParser(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    clientList.add(resultParser(rs));
                }
                return clientList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entry) {

        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    getListOfArg(entityClassMetaDataImple.getFieldsWithoutId(), entry));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entry) {

        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    getListOfArg(entityClassMetaDataImple.getFieldsWithoutId(), entry));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    public List<Object> getListOfArg(List<Field> fields, T entry) {

        List<Object> list = new ArrayList();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                list.add(field.get(entry));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public T resultParser(ResultSet resultSet) {

        Constructor<T> constructor = entityClassMetaDataImple.getConstructor();

        try {
            T inst = constructor.newInstance();
            for (Field field : entityClassMetaDataImple.getAllFields()) {
                Object valueOfField = resultSet.getObject(field.getName());
                field.setAccessible(true);
                field.set(inst, valueOfField);

            }
            return inst;
        } catch (SQLException e) {
            throw new DataTemplateException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

