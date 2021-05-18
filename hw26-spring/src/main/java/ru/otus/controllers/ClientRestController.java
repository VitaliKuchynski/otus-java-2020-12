package ru.otus.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.Client;
import ru.otus.service.DBServiceClient;

@RestController
public class ClientRestController {

    private final DBServiceClient dbServiceClient;

    public ClientRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return  dbServiceClient.getClient(id)
        .orElseThrow(() -> new RuntimeException("Client not found, id:" + id));
    }
}
