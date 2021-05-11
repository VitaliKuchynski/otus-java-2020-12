package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Address;
import ru.otus.repository.AddressRepository;
import ru.otus.sessionManager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DBServiceAddressImpl implements DBServiceAddress {
    private static final Logger log = LoggerFactory.getLogger(DBServiceAddressImpl.class);

    private final AddressRepository addressRepository;
    private final TransactionManager transactionManager;

    public DBServiceAddressImpl(AddressRepository addressRepository, TransactionManager transactionManager) {
        this.addressRepository = addressRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionManager.doInTransaction(() -> {
            var savedAddress = addressRepository.save(address);
            log.info("saved address: {}", savedAddress);
            return  savedAddress;
        });
    }

    @Override
    public Optional<Address> getAddress(long id) {
        var addressOptional = addressRepository.findById(id);
        log.info("saved address: {}", addressOptional);
        return addressOptional;
    }

    @Override
    public List<Address> findAll() {
        var addressList = new ArrayList<Address>();
        addressRepository.findAll().forEach(addressList::add);
        log.info("addressList: {}", addressList);
        return addressList;
    }
}
