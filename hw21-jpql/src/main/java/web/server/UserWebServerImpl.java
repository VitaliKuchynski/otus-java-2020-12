package web.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.crm.service.DbServiceClientImpl;
import web.helpers.FileSystemHelper;
import web.servlet.AuthorizationFilter;
import web.servlet.LoginServlet;
import web.servlet.UsersApiServlet;
import web.servlet.AdminServlet;
import web.webServices.TemplateProcessor;
import web.webServices.UserAuthService;
import com.google.gson.Gson;
import java.util.Arrays;

public class UserWebServerImpl implements UserWebServer {

    private static final String START_PAGE_NAME = "static/index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final Server server;
    private final UserAuthService authService;
    protected final TemplateProcessor templateProcessor;
    private final Gson gson;
    private DbServiceClientImpl dbServiceClient;


    public UserWebServerImpl(int port, UserAuthService authService, DbServiceClientImpl dbServiceClient, Gson gson, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.authService = authService;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {

        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();

    }

    @Override
    public void join() throws Exception {
        server.join();

    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/admin", "/api/user/*"));


        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new AdminServlet(templateProcessor, dbServiceClient)), "/admin");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(dbServiceClient, gson)), "/api/user/*");
        return servletContextHandler;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
