package web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import web.dao.InMemoryUserDao;
import web.dao.UserDao;
import web.server.UserWebServerImpl;
import web.server.UserWebServer;
import web.webServices.TemplateProcessor;
import web.webServices.TemplateProcessorImpl;
import web.webServices.UserAuthService;
import web.webServices.UserAuthServiceImpl;

public class webApp {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UserWebServer usersWebServer = new UserWebServerImpl(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
