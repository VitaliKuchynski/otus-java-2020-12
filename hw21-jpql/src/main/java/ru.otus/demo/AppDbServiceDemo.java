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
import ru.otus.crm.service.DBServicePhoneImpl;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;

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

        var addressTemplate = new DataTemplateHibernate<>(Address.class);

        var phoneTemplate = new DataTemplateHibernate<>(Phone.class);

///

        var dbServicePhone = new DBServicePhoneImpl(transactionManager, phoneTemplate);
        var firstPhone = dbServicePhone.saveEntity(new Phone("87873648284", new Client("Grisha")));
        dbServicePhone.findAll().forEach(phone -> log. info(" >>>>>>>>>>>> phone:{}", phone));



        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var phones = new ArrayList<Phone>();
        var clnt = new Client();

        clnt.setName("Vitali");
        phones.add(new Phone("000000000", clnt));
        phones.add(new Phone("9999999999", clnt));
        phones.add(new Phone("777777777", clnt));
        clnt.setPhoneDataSet(phones);
        clnt.setAddress(new Address("Green str", clnt));
        var clienSecond = dbServiceClient.saveEntity(clnt);
        log. info("..>>>>>>>>>>>>>>>>>>>>>> client:{}:", clienSecond);

//
        var clientSecondSelected = dbServiceClient.getEntity(clienSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clienSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        clientSecondSelected.setAddress(new Address(clientSecondSelected.getAddress().getId(),"Blue line", clientSecondSelected));
        log.info("updatedAddressClientSecondSelected:{}", clientSecondSelected.getAddress());
        clientSecondSelected.setName("Vitali Tokarev");
        dbServiceClient.saveEntity(clientSecondSelected);


        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}