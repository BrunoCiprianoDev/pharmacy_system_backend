package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {

    void validateSupplier(Supplier supplier);

    Supplier saveSupplier(Supplier supplier);

    Supplier updateSupplier(Supplier supplier);

    Page<Supplier> getSupplier(Pageable pageable);

    Supplier getSupplierById(Long id);

    List<Supplier> getSupplierByQuery(String query);

    void deleteSupplier(Long id);

}
