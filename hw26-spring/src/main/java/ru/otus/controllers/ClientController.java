package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.DBServiceClient;

@Controller
public class ClientController {

    private final DBServiceClient dbServiceClient;

    public ClientController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping({"/", "/admin"})
    public String adminView(){
        return "admin.html";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client, @RequestParam("address")String address, @RequestParam("phone")String phone) {
        client.setAddressId(new Address(address));
        client.setPhoneDataSet(new Phone(phone));
        dbServiceClient.saveClient(client);
        return new RedirectView("/", true);
    }
}
