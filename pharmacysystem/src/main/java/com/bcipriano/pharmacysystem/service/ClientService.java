package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public static void validateClient(Client client) {

        String cpfPattern = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
        String phonePattern = "^\\(\\d{2}\\)\\d{4}\\-\\d{4}$";
        String cellPhonePattern = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        LocalDate now = LocalDate.now();
        LocalDate minBirthDate = now.minusYears(120);
        LocalDate maxBirthDate = now.minusYears(18);

        if (client.getName() == null) {
            throw new BusinessRuleException("O campo nome é obrigatório para o cliente.");
        }
        if (client.getCpf() == null) {
            throw new BusinessRuleException("O campo CPF é obrigatório para o CPF");
        }
        if (!client.getCpf().matches(cpfPattern)) {
            throw new BusinessRuleException("O campo CPF do cliente não é válido.");
        }
        if (client.getBornDate() == null) {
            throw new BusinessRuleException("O campo data de nascimento do cliente é obrigatório.");
        }
        if (client.getBornDate().isBefore(minBirthDate) || client.getBornDate().isAfter(maxBirthDate)) {
            throw new BusinessRuleException("A data de nascimento do cliente não é válida.");
        }

        if (client.getAddress() == null) {
            throw new BusinessRuleException("O cliente não tem informações de endereço.");
        }

        AddressService.validateAddress(client.getAddress());

        if (client.getPhone() == null) {
            throw new BusinessRuleException("O campo telefone do cliente é obrigatório.");
        }
        if (!client.getPhone().matches(phonePattern) && !client.getPhone().matches(cellPhonePattern)) {
            throw new BusinessRuleException("O campo telefone do cliente não é válido.");
        }

        if (client.getEmail() == null) {
            throw new BusinessRuleException("O campo e-mail do cliente é obrigatório.");
        }

        if (!client.getEmail().matches(emailPattern)) {
            throw new BusinessRuleException("O e-mail do cliente não é inválido.");
        }

    }

    @Transactional
    public Client saveClient(Client client) {
        if (clientRepository.existsByCpf(client.getCpf())) {
            throw new BusinessRuleException("Já existe um cliente cadastrado com esse cpf.");
        }
        validateClient(client);
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Client client) {
        if (!clientRepository.existsById(client.getId())) {
            throw new NotFoundException("O id do cliente que está tentando modificar não é válido.");
        }

        validateClient(client);

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
