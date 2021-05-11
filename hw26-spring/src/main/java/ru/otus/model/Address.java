package ru.otus.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table("address")
public class Address implements Cloneable {

    @Id
    private Long id;

    @NonNull
    private String street;

    @NonNull
    private Long client_id;

    @PersistenceConstructor
    public Address(Long id, String street, Long client_id) {
        this.id = id;
        this.street = street;
        this.client_id = client_id;
    }

    public Address(String street, Long client_id) {
        this.id = null;
        this.client_id = client_id;
        this.street = street;
    }

    public Address(String name) {
        this.street = name;
        this.id = null;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public Long getId() {
        return id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", client_id=" + client_id +
                '}';
    }
}