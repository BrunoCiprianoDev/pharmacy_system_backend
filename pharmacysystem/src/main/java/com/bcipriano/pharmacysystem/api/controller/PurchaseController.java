package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.PurchaseDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.service.PurchaseService;
import com.bcipriano.pharmacysystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity get() {
        List<Purchase> purchaseList = purchaseService.getPurchase();
        return ResponseEntity.ok(purchaseList.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity get(@PathVariable("query") String query) {
        List<Purchase> purchases = purchaseService.findPurchaseByQuery(query);
        return ResponseEntity.ok(purchases.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity getBySupplierId(@PathVariable("id") Long id){
        List<Purchase> purchases = purchaseService.getPurchaseBySupplierId(id);
        return ResponseEntity.ok(purchases.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
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
