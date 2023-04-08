package com.bcipriano.pharmacysystem.api.controller;


import com.bcipriano.pharmacysystem.api.dto.AddressDTO;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Address> addressPage = addressService.getAddress(pageable);
        return ResponseEntity.ok(addressPage.stream().map(AddressDTO::create).collect(Collectors.toList()));
    }

    public Address converter(AddressDTO addressDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDTO, Address.class);
    }

}
