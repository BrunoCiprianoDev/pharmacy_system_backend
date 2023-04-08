package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.repository.AddressRepository;
import com.bcipriano.pharmacysystem.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional
    public Address saveAddress(Address address) {
        AddressService.validateAddress(address);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address updateAddress(Address address) {
        AddressService.validateAddress(address);
        return addressRepository.save(address);
    }

    @Override
    public Page<Address> getAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @Override
    public Address getAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()){
            throw new InvalidIdException();
        }
        return address.get();
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()){
            throw new InvalidIdException();
        }
        addressRepository.deleteById(id);
    }

}
