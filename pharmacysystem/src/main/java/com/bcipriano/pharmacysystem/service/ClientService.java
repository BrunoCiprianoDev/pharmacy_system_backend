package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    void validateClient(Client client);

    Client saveClient(Client client);

    Client updateClient(Client client);

    Page<Client> getClient(Pageable pageable);

    Client getClientById(Long id);

    List<Client> getClientByQuery(String query);

    void deleteClient(Long id);
}
