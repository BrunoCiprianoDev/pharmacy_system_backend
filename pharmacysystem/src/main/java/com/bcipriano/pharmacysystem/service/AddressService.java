package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {

    static void validateAddress(Address address){
        if(address.getCep() == null || !address.getCep().matches("^\\d{2}\\.\\d{3}\\-\\d{3}$")) {
            throw new BusinessRuleException("Cep inválido.");
        }
        if(address.getUf() == null || address.getUf().length() != 2) {
            throw new BusinessRuleException("UF inválido.");
        }
        if(address.getCity() == null) {
            throw new BusinessRuleException("O campo cidade é obrigatório.");
        }
        if(address.getNeightborhood() == null){
            throw new BusinessRuleException("O campo bairro é obrigatório.");
        }
        if(address.getAddressDetail() == null){
            throw new BusinessRuleException("O campo logradouro é obrigatório.");
        }
        if(address.getNumber() == null){
            throw new BusinessRuleException("O campo númbero é obrigatório.");
        }
        if(address.getComplement() == null) {
            throw new BusinessRuleException("O campo complemento é obrigatório.");
        }
    }

    Address saveAddress(Address address);

    Address updateAddress(Address address);

    Page<Address> getAddress(Pageable pageable);

    Address getAddressById(Long id);

    void deleteAddress(Long id);

}
