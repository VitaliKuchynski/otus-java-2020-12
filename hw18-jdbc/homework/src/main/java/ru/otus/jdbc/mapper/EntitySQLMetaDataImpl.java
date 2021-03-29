package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{

    private EntityClassMetaDataImple entityClassMetaDataImple;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaDataImple){
        this.entityClassMetaDataImple = (EntityClassMetaDataImple) entityClassMetaDataImple;
    }

    @Override
    public String getSelectAllSql() {
        String tableName  = entityClassMetaDataImple.getName().toLowerCase();
        return "select * from " + tableName;
    }

    @Override
    public String getSelectByIdSql() {
        String tableName  = entityClassMetaDataImple.getName().toLowerCase();
        String id = entityClassMetaDataImple.getIdField().getName();
        StringBuilder sb = new StringBuilder();

        sb.append("select * from  ")
                .append(tableName)
                .append(" where ")
                .append(id)
                .append(" = ?");

        return sb.toString();
    }

    @Override
    public String getInsertSql() {
        String name = entityClassMetaDataImple.getName().toLowerCase();
        StringBuilder sb = new StringBuilder();


        sb.append("insert into " + name + " (");
        for (Field field: entityClassMetaDataImple.getFieldsWithoutId()) {
            sb.append(field.getName() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") values (");
        sb.append("?,".repeat(entityClassMetaDataImple.getFieldsWithoutId().size()));
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" )");

        return sb.toString();
    }

    @Override
    public String getUpdateSql() {

        StringBuilder sb = new StringBuilder();
        String tableName = entityClassMetaDataImple.getName().toLowerCase();
        String iD = entityClassMetaDataImple.getIdField().getName();

        sb.append("update ")
                .append(tableName)
                .append("set")
                .append(" (");
        for (Field field: entityClassMetaDataImple.getFieldsWithoutId()) {
            sb.append(field.getName()).
                    append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") where ")
                .append(iD)
                .append("= ? ");

        return sb.toString();
    }
}
