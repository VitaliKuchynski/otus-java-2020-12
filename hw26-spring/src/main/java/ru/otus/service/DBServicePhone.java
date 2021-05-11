package ru.otus.service;

import ru.otus.model.Phone;

import java.util.List;
import java.util.Optional;

public interface DBServicePhone {

    Phone savePhone(Phone phone);

    Optional<Phone> getPhone(long id);

    List<Phone> findAll();
}
