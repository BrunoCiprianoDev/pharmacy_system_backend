package com.bcipriano.pharmacysystem.service;

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

    public Page<Lot> getLot(Pageable pageable) {
        return lotRepository.findAll(pageable);
    }

    public Page<Lot> getLotsByPurchaseId(Long purchaseId, Pageable pageable) {
        return lotRepository.findLotsByPurchaseId(purchaseId, pageable);
    }

    public Page<Lot> getLotsByMerchandiseId(Long merchandiseId, Pageable pageable) {
        return lotRepository.findLotsByMerchandiseId(merchandiseId, pageable);
    }

    public Page<Lot> getLotsByMerchandiseName(String query, Pageable pageable) {
        return lotRepository.findLotsByMerchandiseName(query, pageable);
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

    public Optional<Lot> getLotById(Long id) {
        Optional<Lot> lot = lotRepository.findById(id);
        if (lot.isEmpty()) {
            throw new NotFoundException("Lote com id inválido.");
        }
        return lot;
    }

    @Transactional
    public Lot saveLot(Lot lot) {
        return lotRepository.save(lot);
    }

    @Transactional
    public Lot updateLot(Lot lot) {
        if (!lotRepository.existsById(lot.getId())) {
            throw new NotFoundException("Lot com id inválido.");
        }
        return lotRepository.save(lot);
    }

    @Transactional
    public void deleteLot(Long id) {
        if (!lotRepository.existsById(id)) {
            throw new NotFoundException("Lote com id inválido.");
        }
        lotRepository.deleteById(id);
    }

}