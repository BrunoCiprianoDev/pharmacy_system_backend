package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.model.repository.PurchaseRepository;
import com.bcipriano.pharmacysystem.model.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseService {

    PurchaseRepository purchaseRepository;

    SupplierRepository supplierRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository){
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
    }

    public void validatePurchase(Purchase purchase) {

        if(purchase.getNoteNumber() == null || purchase.getNoteNumber().length() != 9){
            throw new BusinessRuleException("O número da nota fiscal não é valido.(Verifique os nove digitos)");
        }

        if(purchase.getTotal() < 0){
            throw new BusinessRuleException("Valor invalido para o total.");
        }

        if(!supplierRepository.existsById(purchase.getSupplier().getId())){
            throw new BusinessRuleException("Fornecedor inválido.");
        }

    }

    public Purchase savePurchase(Purchase purchase) {
        if(purchaseRepository.existsByNoteNumber(purchase.getNoteNumber())){
            throw new BusinessRuleException("Ja existe uma compra cadastrada com esse número de nota.");
        }
        validatePurchase(purchase);
        return purchaseRepository.save(purchase);
    }

    public Purchase updatePurchase(Purchase purchase) {
        if(!purchaseRepository.existsById(purchase.getId())){
            throw new BusinessRuleException("Compra com id inválido.");
        }

        validatePurchase(purchase);

        Optional<Purchase> purchaseTest = purchaseRepository.findByNoteNumber(purchase.getNoteNumber());
        if(!purchaseTest.isEmpty() && purchaseTest.get().getId() != purchase.getId()){
            throw new BusinessRuleException("Erro ao atualizar o compra. Já existe outra compra com essa nota fiscal.");
        }

        return purchaseRepository.save(purchase);
    }

    public Page<Purchase> getPurchase(Pageable pageable) {
        return purchaseRepository.findAll(pageable);
    }

    public Page<Purchase> getPurchaseBySupplierId(Long id, Pageable pageable){
        return purchaseRepository.findPurchasesBySupplierId(id, pageable);
    }

    public Optional<Purchase> getPurchaseNoteNumber(String noteNumber) {
        if(!purchaseRepository.existsByNoteNumber(noteNumber)){
            throw new BusinessRuleException("Numero da nota fiscal inválido.");
        }
        Optional<Purchase> purchase = purchaseRepository.findByNoteNumber(noteNumber);
        return purchase;
    }

    public Optional<Purchase> getPurchaseById(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        if(purchase.isEmpty()){
            throw new BusinessRuleException("Cliente com id inválido.");
        }
        return purchase;
    }

    public Page<Purchase> findPurchaseByQuery(String query, Pageable pageable) {
        return purchaseRepository.findPurchasesByNoteNumberByQuery(query, pageable);
    }

    public void deletePurchase(Long id) {
        if(!purchaseRepository.existsById(id)) {
            throw new BusinessRuleException("Cliente com id inválido.");
        }
        purchaseRepository.deleteById(id);
    }
}