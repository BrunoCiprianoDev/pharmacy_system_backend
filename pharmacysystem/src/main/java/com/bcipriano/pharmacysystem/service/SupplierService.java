package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public Supplier saveSupplier(Supplier supplier) {
        if (supplierRepository.existsByCnpj(supplier.getCnpj())) {
            throw new BusinessRuleException("Já existe um fornecedor cadastrado com esse cnpj.");
        }

        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        if (!supplierRepository.existsById(supplier.getId())) {
            throw new BusinessRuleException("O fornecedor que está tentando modificar não está cadastrado.");
        }

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
            throw new NotFoundException("Fornecedor com ID inválido.");
        }
        return supplier;
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new NotFoundException("Fornecedor com ID inválido.");
        }
        supplierRepository.deleteById(id);
    }

}
