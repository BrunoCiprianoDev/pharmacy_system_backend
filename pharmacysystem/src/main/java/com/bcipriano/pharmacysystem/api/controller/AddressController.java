package com.bcipriano.pharmacysystem.api.controller;


import com.bcipriano.pharmacysystem.api.dto.AddressDTO;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity get() {
        List<Address> addressList = addressService.getAddress();
        return ResponseEntity.ok(addressList.stream().map(AddressDTO::create).collect(Collectors.toList()));
    }

    public Address converter(AddressDTO addressDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDTO, Address.class);
    }

}
