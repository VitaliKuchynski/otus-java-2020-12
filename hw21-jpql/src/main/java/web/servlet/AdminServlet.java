package web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import web.webServices.TemplateProcessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;


public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";

    private final TemplateProcessor templateProcessor;
    DbServiceClientImpl dbServiceClient;

    public AdminServlet(TemplateProcessor templateProcessor, DbServiceClientImpl dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");

        if(!(name.isEmpty() || address.isEmpty()|| phone.isEmpty())) {
            Client client = new Client();
            client.setName(name);
            client.setAddress(new Address(address, client));
            client.setPhoneDataSet(List.of(new Phone(phone, client)));
            dbServiceClient.saveEntity(client);

            resp.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, Collections.emptyMap()));

        } else {
            PrintWriter writer = resp.getWriter();
            String htmlRespone = "<div>";
            htmlRespone += "<h5 style=\"color:red;\"> Missing required input <br/>";
            htmlRespone += "</div>";

            writer.println(htmlRespone);
            doGet(req, resp);
        }
    }
}
