package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

public interface DBService<T>{

   T saveEntity (T entity);

    Optional<T> getEntity(long id);

    List<T> findAll();
}
