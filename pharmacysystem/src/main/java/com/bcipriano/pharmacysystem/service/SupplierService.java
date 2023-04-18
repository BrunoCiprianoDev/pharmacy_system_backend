package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Client;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SupplierService {

    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public static void validateSupplier(Supplier supplier) {
        String cnpjPattern = "^\\d{2}\\.\\d{3}\\/\\d{4}\\-\\d{2}$";
        String phonePattern = "^\\(\\d{2}\\)\\d{4}\\-\\d{4}$";
        String cellPhonePattern = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (supplier.getName() == null || supplier.getName().trim().equals("")) {
            throw new BusinessRuleException("O campo nome do fornecedor é obrigatório.");
        }
        if (supplier.getCnpj() == null) {
            throw new BusinessRuleException("O campo CNPJ do fornecedor é obrigatório.");
        }
        if (!supplier.getCnpj().matches(cnpjPattern)) {
            throw new BusinessRuleException("CNPJ do fornecedor inválido.");
        }
        if (supplier.getAddress() == null) {
            throw new BusinessRuleException("Endereço do fornecedor inválido.");
        }

        AddressService.validateAddress(supplier.getAddress());

        if (supplier.getPrimaryPhone() == null) {
            throw new BusinessRuleException("O campo telefone 1 do fornecedor é obrigatório.");
        }
        if (!supplier.getPrimaryPhone().matches(phonePattern) && !supplier.getPrimaryPhone().matches(cellPhonePattern)) {
            throw new BusinessRuleException("Telefone 1 do fornecedor inválido.");
        }
        if (supplier.getSecundaryPhone() != null) {
            if (!supplier.getSecundaryPhone().matches(phonePattern) && !supplier.getSecundaryPhone().matches(cellPhonePattern)) {
                throw new BusinessRuleException("Telefone 2 do fornecedor inválido.");
            }
        }
        if (supplier.getEmail() == null) {
            throw new BusinessRuleException("O campo e-mail é do fornecedor obrigatório.");
        }
        if (!supplier.getEmail().matches(emailPattern)) {
            throw new BusinessRuleException("E-mail do fornecedor inválido.");
        }
    }

    @Transactional
    public Supplier saveSupplier(Supplier supplier) {
        if (supplierRepository.existsByCnpj(supplier.getCnpj())) {
            throw new BusinessRuleException("Já existe um fornecedor cadastrado com esse cnpj.");
        }
        validateSupplier(supplier);
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        if (!supplierRepository.existsById(supplier.getId())) {
            throw new BusinessRuleException("O fornecedor que está tentando modificar não está cadastrado.");
        }

        validateSupplier(supplier);

        Optional<Supplier> supplierTest = supplierRepository.findByCnpj(supplier.getCnpj());
        if (!supplierTest.isEmpty() && supplierTest.get().getId() != supplier.getId()) {
            throw new BusinessRuleException("Erro ao atualizar o fornecedor. Já existe outro fornecedor com esse CNPJ.");
        }
        return supplierRepository.save(supplier);
    }

    public Page<Supplier> getSupplier(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    public Page<Supplier> getSupplierByQuery(String query, Pageable pageable) {
        return supplierRepository.findSupplierByQuery(query, pageable);
    }

    public Optional<Supplier> getSupplierById(Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isEmpty()) {
            throw new NotFoundException("O id do fornecedor que está tentando buscar não é válido.");
        }
        return supplier;
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new NotFoundException("O id do fornecedor que está tentando excluir não é válido.");
        }
        supplierRepository.deleteById(id);
    }

}
