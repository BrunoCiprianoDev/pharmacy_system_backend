package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;
import com.bcipriano.pharmacysystem.service.AddressService;
import com.bcipriano.pharmacysystem.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void validateSupplier(Supplier supplier) {
        String cnpjPattern = "^\\d{2}\\.\\d{3}\\/\\d{4}\\-\\d{2}$";
        String phonePattern = "^\\(\\d{2}\\)\\d{4}\\-\\d{4}$";
        String cellPhonePattern = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if(supplier.getName() == null || supplier.getName().trim().equals("")){
            throw new BusinessRuleException("O campo nome é obrigatório.");
        }
        if(supplier.getCnpj() == null){
            throw new BusinessRuleException("O campo CNPJ é obrigatório.");
        }
        if(!supplier.getCnpj().matches(cnpjPattern)){
            throw new BusinessRuleException("CNPJ inválido.");
        }
        if(supplier.getAddress() == null){
            throw new BusinessRuleException("Endereço inválido.");
        }

        AddressService.validateAddress(supplier.getAddress());

        if(supplier.getPrimaryPhone() == null){
            throw new BusinessRuleException("O campo telefone 1 é obrigatório.");
        }
        if(!supplier.getPrimaryPhone().matches(phonePattern) && !supplier.getPrimaryPhone().matches(cellPhonePattern)){
            throw new BusinessRuleException("Telefone 1 inválido.");
        }
        if(supplier.getSecundaryPhone() != null) {
            if(!supplier.getSecundaryPhone().matches(phonePattern) && !supplier.getSecundaryPhone().matches(cellPhonePattern)){
                throw new BusinessRuleException("Telefone 2 inválido.");
            }
        }
        if(supplier.getEmail() == null){
            throw new BusinessRuleException("O campo e-mail é obrigatório.");
        }
        if(!supplier.getEmail().matches(emailPattern)){
            throw new BusinessRuleException("E-mail inválido.");
        }
    }

    @Override
    public Supplier saveSupplier(Supplier supplier) {
        if(supplierRepository.existsByCnpj(supplier.getCnpj())){
            throw new BusinessRuleException("Já existe um fornecedor cadastrado com esse cnpj.");
        }
        if(supplierRepository.existsByName(supplier.getName())){
            throw new BusinessRuleException("Já existe um fornecedor cadastrado com esse nome.");
        }
        validateSupplier(supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) {
        if(!supplierRepository.existsById(supplier.getId())){
            throw new BusinessRuleException("O fornecedor que está tentando modificar não está cadastrado.");
        }
        validateSupplier(supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> getSupplier() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(supplier.isEmpty()){
            throw new BusinessRuleException("Fornecedor com id inválido.");
        }
        return supplier.get();
    }

    @Override
    public List<Supplier> getSupplierByQuery(String query) {
        return supplierRepository.findSupplierByQuery(query);
    }

    @Override
    public void deleteSupplier(Long id) {
        if(!supplierRepository.existsById(id)) {
            throw new BusinessRuleException("Fornecedor com id inválido.");
        }
        supplierRepository.deleteById(id);
    }
}
