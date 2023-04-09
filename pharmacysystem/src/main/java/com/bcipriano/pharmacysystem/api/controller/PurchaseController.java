package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.PurchaseDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.service.PurchaseService;
import com.bcipriano.pharmacysystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Purchase> purchasePage = purchaseService.getPurchase(pageable);
        return ResponseEntity.ok(purchasePage.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        int startItem = size * page;
        List<Purchase> pageList;
        List<Purchase> purchaseList = purchaseService.findPurchaseByQuery(query);

        if(purchaseList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int index = Math.min(startItem + size, purchaseList.size());
            pageList = purchaseList.subList(startItem, index);
        }

        return ResponseEntity.ok(pageList.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity getBySupplierId(@PathVariable("id") long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size){

        int startItem = size * page;
        List<Purchase> pageList;
        List<Purchase> purchaseList = purchaseService.getPurchaseBySupplierId(id);

        if(purchaseList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int index = Math.min(startItem + size, purchaseList.size());
            pageList = purchaseList.subList(startItem, index);
        }

        return ResponseEntity.ok(pageList.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id){
        try {
            Purchase purchaseResponse = purchaseService.getPurchaseById(id);
            PurchaseDTO purchaseDTO = PurchaseDTO.create(purchaseResponse);
            return ResponseEntity.ok(purchaseDTO);
        } catch(BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            Purchase purchase = converter(purchaseDTO);
            purchaseService.savePurchase(purchase);
            return new ResponseEntity("Compra armazenada com sucesso!", HttpStatus.CREATED);
        } catch(BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody PurchaseDTO purchaseDTO){
        try {
            Purchase purchase = converter(purchaseDTO);
            purchase.setId(id);
            purchaseService.updatePurchase(purchase);
            return ResponseEntity.ok("Compra atualizada com sucesso!");
        } catch(BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        try {
            purchaseService.deletePurchase(id);
            return ResponseEntity.ok("Compra excluída com sucesso!");
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Purchase converter(PurchaseDTO purchaseDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Purchase purchase = modelMapper.map(purchaseDTO, Purchase.class);

        purchase.setPurchaseDate(LocalDate.parse(purchaseDTO.getPurchaseDate()));

        if(purchaseDTO.getSupplierId() != null) {
            purchase.setSupplier(supplierService.getSupplierById(purchaseDTO.getSupplierId()));
        }

        return purchase;
    }
}
