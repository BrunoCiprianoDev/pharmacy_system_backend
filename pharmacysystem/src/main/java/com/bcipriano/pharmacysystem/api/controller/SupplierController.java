package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.EmployeeDTO;
import com.bcipriano.pharmacysystem.api.dto.SupplierDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Supplier> supplierPage = supplierService.getSupplier(pageable);
        return ResponseEntity.ok(supplierPage.stream().map(SupplierDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Supplier> supplierPage = supplierService.getSupplierByQuery(query, pageable);
        return ResponseEntity.ok(supplierPage.stream().map(SupplierDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Optional<Supplier> supplierResponse = supplierService.getSupplierById(id);
            return ResponseEntity.ok(supplierResponse.map(SupplierDTO::create));
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier supplier = converter(supplierDTO);
            supplierService.saveSupplier(supplier);
            return new ResponseEntity("Fornecedor armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return new ResponseEntity(businessRuleException.getMessage(), businessRuleException.getStatus());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier supplier = converter(supplierDTO);
            supplier.setId(id);
            supplierService.updateSupplier(supplier);
            return ResponseEntity.ok("Fornecedor atualizado com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return new ResponseEntity(businessRuleException.getMessage(), businessRuleException.getStatus());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("Fornecedor excluído com sucesso!");
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    public static Supplier converter(SupplierDTO supplierDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);

        Address address = modelMapper.map(supplierDTO, Address.class);
        supplier.setAddress(address);
        return supplier;
    }

}

