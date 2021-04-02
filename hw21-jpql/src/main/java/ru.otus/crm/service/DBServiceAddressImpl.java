package ru.otus.crm.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;

import java.util.List;
import java.util.Optional;

public class DBServiceAddressImpl implements DBService<Address>{

    private static final Logger log = LoggerFactory.getLogger(DBServiceAddressImpl.class);

    private final DataTemplate<Address> addressDataSetDataTemplate;
    private final TransactionManager transactionManager;

    public DBServiceAddressImpl(TransactionManager transactionManager, DataTemplate<Address> addressDataSetDataTemplate) {
        this.addressDataSetDataTemplate = addressDataSetDataTemplate;
        this.transactionManager = transactionManager;
    }


    @Override
    public Address saveEntity(Address address) {
         return transactionManager.doInTransaction(session -> {
            var addressCloned = address.clone();
            if (address.getId() == null) {
                addressDataSetDataTemplate.insert(session, addressCloned);
                log.info("created address: {}", addressCloned);
                return addressCloned;
            }
             addressDataSetDataTemplate.update(session, addressCloned);
            log.info("updated addres: {}", addressCloned);
            return addressCloned;
        });
    }

    @Override
    public Optional<Address> getEntity(long id) {
        return transactionManager.doInTransaction(session -> {
            var addressOptional = addressDataSetDataTemplate.findById(session, id);
            log.info("address: {}", addressOptional);
            return addressOptional;
        });
    }

    @Override
    public List<Address> findAll() {
        return transactionManager.doInTransaction(session -> {
            var addressList = addressDataSetDataTemplate.findAll(session);
            log.info("clientList:{}", addressList);
            return addressList;
        });
    }
}
