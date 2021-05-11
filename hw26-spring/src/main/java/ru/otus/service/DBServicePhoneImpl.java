package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Phone;
import ru.otus.repository.PhoneRepository;
import ru.otus.sessionManager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DBServicePhoneImpl implements DBServicePhone{
    private static final Logger log = LoggerFactory.getLogger(DBServicePhoneImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepository phoneRepository;

    public DBServicePhoneImpl(TransactionManager transactionManager, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone savePhone(Phone phone) {
        return transactionManager.doInTransaction(() -> {
            var savedPhone = phoneRepository.save(phone);
            log.info("saved client: {}", savedPhone);
            return savedPhone;
        });
    }

    @Override
    public Optional<Phone> getPhone(long id) {
        return transactionManager.doInTransaction(() -> {
            var selectedPhone = phoneRepository.findById(id);
            log.info("phone {}" , selectedPhone);
            return selectedPhone;
        });
    }

    @Override
    public List<Phone> findAll() {
        var phoneList = new ArrayList<Phone>();
        phoneRepository.findAll().forEach(phoneList::add);
        log.info("phoneList:{}", phoneList);
        return phoneList;
    }
}
