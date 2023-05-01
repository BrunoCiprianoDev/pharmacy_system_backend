package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Transactional
    public Client saveClient(Client client) {
        if (clientRepository.existsByCpf(client.getCpf())) {
            throw new BusinessRuleException("Já existe um cliente cadastrado com esse cpf.");
        }
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Client client) {
        if (!clientRepository.existsById(client.getId())) {
            throw new NotFoundException("O id do cliente que está tentando modificar não é válido.");
        }

        Optional<Client> clientTest = clientRepository.findByCpf(client.getCpf());
        if (!clientTest.isEmpty() && clientTest.get().getId() != client.getId()) {
            throw new BusinessRuleException("Erro ao atualizar o cliente. Já existe outro cliente com esse CPF.");
        }

        return clientRepository.save(client);
    }

    @Transactional
    public Page<Client> getClient(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Transactional
    public Optional<Client> getClientById(Long id) {
        Optional<Client> client = clientRepository.findClientById(id);
        if (client.isEmpty()) {
            throw new NotFoundException("O id do cliente que está tentando buscar não é válido.");
        }
        return client;
    }

    @Transactional
    public Page<Client> getClientsByQuery(String query, Pageable pageable) {
        return clientRepository.findClientsByQuery(query, pageable);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("O id do cliente que está tentando excluir não é válido.");
        }
        clientRepository.deleteById(id);
    }

}
