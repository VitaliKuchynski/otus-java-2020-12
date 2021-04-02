package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;

public class DBServicePhoneImpl implements DBService<Phone>{


    private static final Logger log = LoggerFactory.getLogger(DBServicePhoneImpl.class);

    private final DataTemplate<Phone> addressDataSetDataTemplate;
    private final TransactionManager transactionManager;

    public DBServicePhoneImpl(TransactionManager transactionManager, DataTemplate<Phone> addressDataSetDataTemplate) {
        this.addressDataSetDataTemplate = addressDataSetDataTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Phone saveEntity(Phone phone) {
        return transactionManager.doInTransaction(session -> {
            var phoneCloned = phone.clone();
            if (phone.getId() == null) {
                addressDataSetDataTemplate.insert(session, phoneCloned);
                log.info("created phone: {}", phoneCloned);
                return phoneCloned;
            }
            addressDataSetDataTemplate.update(session, phoneCloned);
            log.info("updated phone: {}", phoneCloned);
            return phoneCloned;
        });
    }

    @Override
    public Optional<Phone> getEntity(long id) {
        return transactionManager.doInTransaction(session -> {
            var addressOptional = addressDataSetDataTemplate.findById(session, id);
            log.info("phone: {}", addressOptional);
            return addressOptional;
        });
    }

    @Override
    public List<Phone> findAll() {
        return transactionManager.doInTransaction(session -> {
            var phoneList = addressDataSetDataTemplate.findAll(session);
            log.info("phoneList:{}", phoneList);
            return phoneList;
        });
    }
}
