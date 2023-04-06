package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Supplier;

import java.util.List;

public interface SupplierService {

    void validateSupplier(Supplier supplier);

    Supplier saveSupplier(Supplier supplier);

    Supplier updateSupplier(Supplier supplier);

    List<Supplier> getSupplier();

    Supplier getSupplierById(Long id);

    List<Supplier> getSupplierByQuery(String query);

    void deleteSupplier(Long id);

}
