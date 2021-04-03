package ru.otus.crm.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.name = name;
    }

    public Client(Long id, String name, List<Phone> phones, Address address) {
        this.id = id;
        this.name = name;
        this.phones = phones;
        this.address = address;
    }

    public Client(String name, List<Phone> phones) {
        this.name = name;
        this.phones = phones;
    }

    @Override
    public Client clone() {

        Client client = new Client(this.id, this.name, this.phones, this.address);

        for(Phone phone : client.getPhoneDataSet()) {
            phone.setClient(client);
        }
        address.setClient(client);

        return client;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhoneDataSet() {
        return phones;
    }

    public void setPhoneDataSet(List<Phone> phone) {
        this.phones = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}