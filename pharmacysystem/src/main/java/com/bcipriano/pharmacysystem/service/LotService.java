package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;

import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.MerchandiseRepository;
import com.bcipriano.pharmacysystem.model.repository.PurchaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LotService {

    LotRepository lotRepository;

    PurchaseRepository purchaseRepository;

    MerchandiseRepository merchandiseRepository;

    public LotService(LotRepository lotRepository, PurchaseRepository purchaseRepository, MerchandiseRepository merchandiseRepository) {
        this.lotRepository = lotRepository;
        this.purchaseRepository = purchaseRepository;
        this.merchandiseRepository = merchandiseRepository;
    }

    public void validateLot(Lot lot) {

        if (lot.getNumber() == null || lot.getNumber().trim().equals("")) {
            throw new BusinessRuleException("Lot inválido.");
        }
        if (lot.getExpirationDate() == null) {
            throw new BusinessRuleException("Data de válidade inválida.");
        }
        if (lot.getMerchandise() == null || !merchandiseRepository.existsById(lot.getMerchandise().getId())) {
            throw new BusinessRuleException("Mercadoria inválida");
        }
        if (lot.getUnits() < 0) {
            throw new BusinessRuleException("Número de unidades inválido.");
        }

    }

    @Transactional
    public Lot saveLot(Lot lot) {
        validateLot(lot);
        return lotRepository.save(lot);
    }

    @Transactional
    public Lot updateLot(Lot lot) {
        if (!lotRepository.existsById(lot.getId())) {
            throw new BusinessRuleException("Lot com id inválido.");
        }
        validateLot(lot);
        return lotRepository.save(lot);
    }

    public Optional<Lot> getLotById(Long id) {
        Optional<Lot> lot = lotRepository.findById(id);
        if (lot.isEmpty()) {
            throw new BusinessRuleException("Lote com id inválido.");
        }
        return lot;
    }

    public Page<Lot> getLot(Pageable pageable) {
        return lotRepository.findAll(pageable);
    }

    public Page<Lot> getLotsByPurchaseId(Long purchaseId, Pageable pageable) {
        return lotRepository.findLotsByPurchaseId(purchaseId, pageable);
    }

    public Page<Lot> getLotsByMerchandiseId(Long merchandiseId, Pageable pageable) {
        return lotRepository.findLotsByMerchandiseId(merchandiseId, pageable);
    }

    public Page<Lot> getLotsByPurchaseNoteNumber(String query, Pageable pageable) {
        return lotRepository.findLotsByLotNumber(query, pageable);
    }

    public Integer getCurrentStockByMerchandiseId(Long merchandiseId) {
        if(!merchandiseRepository.existsById(merchandiseId)){
            throw new NotFoundException("Mercadoria não encontrada.");
        }
        return lotRepository.sumUnitsByMerchandiseId(merchandiseId);
    }

    public Page<Lot> getLotExpiringWithinDays(Integer days, Pageable pageable){
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(days);
        return lotRepository.findLotsExpiringWithinNDays(now, endDate, pageable);
    }

    @Transactional
    public void deleteLot(Long id) {
        if (!lotRepository.existsById(id)) {
            throw new BusinessRuleException("Lote com id inválido.");
        }
        lotRepository.deleteById(id);
    }

}