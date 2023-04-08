package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.ClientDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.service.AddressService;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private final AddressService addressService;

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

        int startItem = size * page;
        List<Client> pageList;
        List<Client> clients = clientService.getClientByQuery(query);

        if(clients.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + size, clients.size());
            pageList = clients.subList(startItem, toIndex);
        }

        return ResponseEntity.ok(pageList.stream().map(ClientDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id){
        try {
            Client clientResponse = clientService.getClientById(id);
            ClientDTO clientDTO = ClientDTO.create(clientResponse);
            return ResponseEntity.ok(clientDTO);
        } catch(BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ClientDTO clientDTO) {
        try {
            Client client = converter(clientDTO);
            Address address = addressService.saveAddress(client.getAddress());
            client.setAddress(address);
            clientService.saveClient(client);
            return new ResponseEntity("Cliente armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody ClientDTO clientDTO) {
        try {
            Client client = converter(clientDTO);
            client.setId(id);
            clientService.updateClient(client);
            return ResponseEntity.ok("Cliente atualizado com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok("Cliente excluído com sucesso!");
        } catch(BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Client converter(ClientDTO clientDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Client client = modelMapper.map(clientDTO, Client.class);

        client.setBornDate(LocalDate.parse(clientDTO.getBornDate()));

        Address address = modelMapper.map(clientDTO, Address.class);
        client.setAddress(address);
        return client;
    }


}
