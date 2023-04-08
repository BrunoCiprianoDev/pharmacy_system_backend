package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.SupplierDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.service.AddressService;
import com.bcipriano.pharmacysystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Supplier> supplierPage = supplierService.getSupplier(pageable);
        return ResponseEntity.ok(supplierPage.stream().map(SupplierDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size){

        int startItem = size * page;
        List<Supplier> pageList;
        List<Supplier> suppliers = supplierService.getSupplierByQuery(query);

        if(suppliers.size() < startItem){
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + size, suppliers.size());
            pageList = suppliers.subList(startItem, toIndex);
        }
        return ResponseEntity.ok(pageList.stream().map(SupplierDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Supplier supplierResponse = supplierService.getSupplierById(id);
            SupplierDTO supplierDTO = SupplierDTO.create(supplierResponse);
            return ResponseEntity.ok(supplierDTO);
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody SupplierDTO supplierDTO) {
        try {
            Supplier supplier = converter(supplierDTO);
            Address address = addressService.saveAddress(supplier.getAddress());
            supplier.setAddress(address);
            supplierService.saveSupplier(supplier);
            return new ResponseEntity("Fornecedor armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody SupplierDTO supplierDTO){
        try {
            Supplier supplier = converter(supplierDTO);
            supplier.setId(id);
            supplierService.updateSupplier(supplier);
            return ResponseEntity.ok("Fornecedor atualizado com sucesso!");
        } catch(BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("Fornecedor excluído com sucesso!");
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Supplier converter(SupplierDTO supplierDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);

        Address address = modelMapper.map(supplierDTO, Address.class);
        supplier.setAddress(address);
        return supplier;
    }

}
