package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.DBServiceAddress;
import ru.otus.service.DBServiceClient;
import ru.otus.service.DBServicePhone;

import java.util.HashSet;

@Component("actions")
public class Actions {

    private static final Logger log = LoggerFactory.getLogger(Actions.class);

    private final DBServiceClient dbServiceClient;
    private final DBServicePhone dbServicePhone;
    private final DBServiceAddress dbServiceAddress;

    public Actions(DBServiceClient dbServiceClient, DBServicePhone dbServicePhone, DBServiceAddress dbServiceAddress) {
        this.dbServiceClient = dbServiceClient;
        this.dbServicePhone = dbServicePhone;
        this.dbServiceAddress = dbServiceAddress;
    }

    void action() {

        var firstClient = dbServiceClient.saveClient(new Client("Grisha", new HashSet<>(), new HashSet<>()));
        var id = firstClient.getId();
        dbServicePhone.savePhone(new Phone("84875375", id));
        dbServiceAddress.saveAddress(new Address("TEST STR", id));

        var secondClient = dbServiceClient.saveClient(new Client("Vitali", new HashSet<>(), new HashSet<>()));
        var idSecond = secondClient.getId();
        dbServicePhone.savePhone(new Phone("9999999", idSecond));
        dbServiceAddress.saveAddress(new Address("Green STR", idSecond));
}
}
