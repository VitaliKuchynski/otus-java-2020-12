package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.List;

public class AppDbServiceDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger log = LoggerFactory.getLogger(AppDbServiceDemo.class);

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
//
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var phones = new ArrayList<Phone>();
        var clnt = new Client();

        clnt.setName("Vitali");
        phones.add(new Phone("000000000", clnt));
        phones.add(new Phone("9999999999", clnt));
        phones.add(new Phone("777777777", clnt));
        clnt.setPhoneDataSet(phones);
        clnt.setAddress(new Address("Green str", clnt));
        var clientFirst = dbServiceClient.saveEntity(clnt);
        log. info("..>>>>>>>>>>>>>>>>>>>>>> client:{}: ", clientFirst);

        var clnt2 = new Client();
        clnt2.setName("Grisha");
        clnt2.setAddress(new Address("Main str", clnt2));
        clnt2.setPhoneDataSet(List.of(new Phone("33333333", clnt2)));
        var clientNext = dbServiceClient.saveEntity(clnt2);
        log. info("..>>>>>>>>>>>>>>>>>>>>>> client:{}: ", clientNext);

//
        var clientSecondSelected = dbServiceClient.getEntity(clientFirst.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientFirst.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        clientSecondSelected.setAddress(new Address(clientSecondSelected.getAddress().getId(),"Blue line", clientSecondSelected));
        log.info("updatedAddressClientSecondSelected:{}", clientSecondSelected.getAddress());
        clientSecondSelected.setName("Vitali Tokarev");
        dbServiceClient.saveEntity(clientSecondSelected);


        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}