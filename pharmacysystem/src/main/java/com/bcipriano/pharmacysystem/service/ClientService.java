package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Client;

import java.util.List;

public interface ClientService {

    void validateClient(Client client);

    Client saveClient(Client client);

    Client updateClient(Client client);

    List<Client> getClient();

    Client getClientById(Long id);

    List<Client> getClientByQuery(String query);

    void deleteClient(Long id);


}
