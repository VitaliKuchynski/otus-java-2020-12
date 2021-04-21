package web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import web.dao.InMemoryUserDao;
import web.dao.UserDao;
import web.server.UserWebServer;
import web.server.UserWebServerImpl;
import web.webServices.TemplateProcessor;
import web.webServices.TemplateProcessorImpl;
import web.webServices.UserAuthService;
import web.webServices.UserAuthServiceImpl;

import java.util.List;

public class webApp {

    private static final int WEB_SERVER_PORT = 8081;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

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
        DbServiceClientImpl dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var clnt2 = new Client();
        clnt2.setName("Grisha");
        clnt2.setAddress(new Address("Main str", clnt2));
        clnt2.setPhoneDataSet(List.of(new Phone("33333333", clnt2)));
        dbServiceClient.saveEntity(clnt2);

        var clnt3 = new Client();
        clnt3.setName("Vitali Bublikov");
        clnt3.setAddress(new Address("Green str", clnt3));
        clnt3.setPhoneDataSet(List.of(new Phone("33333333", clnt3)));
        dbServiceClient.saveEntity(clnt3);


        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UserWebServer usersWebServer = new UserWebServerImpl(WEB_SERVER_PORT,
                authService, dbServiceClient, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
