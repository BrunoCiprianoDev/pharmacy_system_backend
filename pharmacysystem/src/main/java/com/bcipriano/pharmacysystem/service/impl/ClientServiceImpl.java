package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.repository.ClientRepository;
import com.bcipriano.pharmacysystem.service.AddressService;
import com.bcipriano.pharmacysystem.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }
    @Override
    public void validateClient(Client client) {

        String cpfPattern = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
        String phonePattern = "^\\(\\d{2}\\)\\d{4}\\-\\d{4}$";
        String cellPhonePattern = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        LocalDate now = LocalDate.now();
        LocalDate minBirthDate = now.minusYears(120);
        LocalDate maxBirthDate = now.minusYears(18);

        if(client.getName() == null) {
            throw new BusinessRuleException("O campo nome é obrigatório.");
        }
        if(client.getCpf() == null){
            throw new BusinessRuleException("O campo CPF é obrigatório.");
        }
        if(!client.getCpf().matches(cpfPattern)) {
            throw new BusinessRuleException("CPF inválido.");
        }
        if(client.getBornDate() == null){
            throw new BusinessRuleException("O campo data de nascimento é obrigatório.");
        }
        if (client.getBornDate().isBefore(minBirthDate) || client.getBornDate().isAfter(maxBirthDate)) {
            throw new BusinessRuleException("Data de nascimento inválida.");
        }

        if(client.getAddress() == null) {
            throw new BusinessRuleException("Endereço inválido.");
        }

        AddressService.validateAddress(client.getAddress());

        if(client.getPhone() == null){
            throw new BusinessRuleException("O campo telefone é obrigatório.");
        }
        if(!client.getPhone().matches(phonePattern) && !client.getPhone().matches(cellPhonePattern)){
            throw new BusinessRuleException("Telefone inválido.");
        }

        if(client.getEmail() == null){
            throw new BusinessRuleException("O campo e-mail é obrigatório.");
        }

        if(!client.getEmail().matches(emailPattern)){
            throw new BusinessRuleException("E-mail inválido.");
        }

    }

    @Override
    @Transactional
    public Client saveClient(Client client) {
        if(clientRepository.existsByCpf(client.getCpf())){
            throw new BusinessRuleException("Já existe um cliente cadastrado com esse cpf.");
        }
        validateClient(client);
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Client client) {
        if(!clientRepository.existsById(client.getId())){
            throw new BusinessRuleException("O cliente que está tentando modificar não está cadastrado.");
        }
        validateClient(client);
        return clientRepository.save(client);
    }

    @Override
    public Page<Client> getClient(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    public Client getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if(client.isEmpty()){
            throw new BusinessRuleException("Client com id inválido.");
        }
        return client.get();
    }

    @Override
    public List<Client> getClientByQuery(String query) {
        return clientRepository.findClientsByQuery(query);
    }

    @Override
    public void deleteClient(Long id) {
        boolean exists = clientRepository.existsById(id);
        if(!exists){
            throw new BusinessRuleException("Cliente com id inválido.");
        }
        clientRepository.deleteById(id);
    }
}
