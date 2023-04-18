package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.ClientDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> clientPage = clientService.getClient(pageable);
        return ResponseEntity.ok(clientPage.stream().map(ClientDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> clientPage = clientService.getClientsByQuery(query, pageable);
        return ResponseEntity.ok(clientPage.stream().map(ClientDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Optional<Client> clientResponse = clientService.getClientById(id);
            return ResponseEntity.ok(clientResponse.map(ClientDTO::create));
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody ClientDTO clientDTO) {
        try {
            Client client = converter(clientDTO);
            clientService.saveClient(client);
            return new ResponseEntity("Cliente armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException){
            return new ResponseEntity(businessRuleException.getMessage(), businessRuleException.getStatus());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody ClientDTO clientDTO) {
        try {
            Client client = converter(clientDTO);
            client.setId(id);
            clientService.updateClient(client);
            return ResponseEntity.ok("Cliente atualizado com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return new ResponseEntity(businessRuleException.getMessage(), businessRuleException.getStatus());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok("Cliente excluído com sucesso!");
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    public static Client converter(ClientDTO clientDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Client client = modelMapper.map(clientDTO, Client.class);

        client.setBornDate(LocalDate.parse(clientDTO.getBornDate()));

        Address address = modelMapper.map(clientDTO, Address.class);
        client.setAddress(address);
        return client;
    }

}
