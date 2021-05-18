package ru.otus.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Table("client")
public class Client {

    @Id
    private Long id;

    @NonNull
    private String name;

    @MappedCollection(idColumn = "client_id")
    private Set<Address> addressId;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phoneIds;


    @PersistenceConstructor
    public Client(Long id, String name, Set<Address> addressId, Set<Phone> phoneIds) {
        this.id = id;
        this.name = name;
        this.phoneIds = phoneIds;
        this.addressId = addressId;
    }

    public Client(String name, Set<Address> addressId, Set<Phone> phoneIds) {
        this.id = null;
        this.name = name;
        this.addressId = addressId;
        this.phoneIds = phoneIds;
    }

    public Client() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Address> getAddressId() {
        return addressId;
    }

    public void setAddressId(Address address) {
       addressId = new HashSet<>();
       addressId.add(address);
    }

    public Set<Phone> getPhoneDataSet() {
        return phoneIds;
    }

    public void setPhoneDataSet(Phone phone) {
        phoneIds = new HashSet<>();
        phoneIds.add(phone);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressId='" + addressId + '\'' +
                ", phoneIds=" + phoneIds +
                '}';
    }
}