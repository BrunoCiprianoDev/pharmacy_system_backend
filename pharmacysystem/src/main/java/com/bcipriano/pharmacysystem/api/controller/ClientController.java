package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.ClientDTO;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.service.ClientService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/clients", produces = {"application/json"})
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @ApiOperation(value = "Returns all clients registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of clients"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<ClientDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of clients to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Client> clientPage = clientService.getClient(pageable);
        return ResponseEntity.ok(clientPage.stream().map(ClientDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of clients based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of clients that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<ClientDTO>> get(
            @ApiParam(value = "The query to search for clients")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of clients to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Client> clientPage = clientService.getClientsByQuery(query, pageable);
        return ResponseEntity.ok(clientPage.stream().map(ClientDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Returns clients by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return client by ID."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No clients found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ClientDTO> get(
            @ApiParam(value = "Client ID", required = true)
            @PathVariable("id") Long id
    ) {
        Optional<Client> clientResponse = clientService.getClientById(id);
        return ResponseEntity.ok(ClientDTO.create(clientResponse.get()));
    }

    @PostMapping
    @ApiOperation(value = "Creates a new client")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Client created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ClientDTO> post(
            @ApiParam(value = "The details of the client to create", required = true)
            @RequestBody @Valid ClientDTO clientDTO
    ) {
            Client client = converter(clientDTO);
            Client clientResponse = clientService.saveClient(client);
            return new ResponseEntity(ClientDTO.create(clientResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update an client")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ClientDTO> put(
            @ApiParam(value = "The details of the client to update", required = true)
            @PathVariable("id") Long id,
            @RequestBody @Valid ClientDTO clientDTO
    ) {
        Client client = converter(clientDTO);
        client.setId(id);
        Client clientResponse = clientService.updateClient(client);
        return new ResponseEntity(ClientDTO.create(clientResponse), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete an client by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Client ID", required = true)
            @PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Cliente excluído com sucesso!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                }
        );
        return errors;
    }

    public static Client converter(ClientDTO clientDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Client client = modelMapper.map(clientDTO, Client.class);

        client.setBirthDate(LocalDate.parse(clientDTO.getBirthDate()));

        Address address = modelMapper.map(clientDTO, Address.class);
        client.setAddress(address);
        return client;
    }

}
