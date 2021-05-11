package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;


@Table("phone")
public class Phone {

    @Id
    private Long id;

    @NonNull
    private String number;

    @NonNull
    private Long client_id;

    @PersistenceConstructor
    public Phone(Long id, String number, Long client_id) {
        this.id = id;
        this.number = number;
        this.client_id = client_id;
    }

    public Phone(String number, Long client_id) {
        this.id = null;
        this.number = number;
        this.client_id = client_id;
    }

    public Phone(String number) {
        this.id = null;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getClient() {
        return client_id;
    }

    public void setClient(Long client) {
        this.client_id = client;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", client_id=" + client_id +
                '}';
    }
}