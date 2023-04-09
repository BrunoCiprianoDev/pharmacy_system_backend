package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.MerchandiseRepository;
import com.bcipriano.pharmacysystem.model.repository.PurchaseRepository;
import com.bcipriano.pharmacysystem.service.LotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LotServiceImpl implements LotService {

    LotRepository lotRepository;

    PurchaseRepository purchaseRepository;

    MerchandiseRepository merchandiseRepository;

    public LotServiceImpl(LotRepository lotRepository, PurchaseRepository purchaseRepository, MerchandiseRepository merchandiseRepository){
        this.lotRepository = lotRepository;
        this.purchaseRepository = purchaseRepository;
        this.merchandiseRepository = merchandiseRepository;
    }

    @Override
    public void validateLot(Lot lot) {

        if(lot.getNumber() == null || lot.getNumber().trim().equals("")){
            throw new BusinessRuleException("Lot inválido.");
        }
        if(lot.getExpirationDate() == null){
            throw new BusinessRuleException("Data de válidade inválida.");
        }
        if(lot.getMerchandise() == null || !merchandiseRepository.existsById(lot.getMerchandise().getId())){
            throw new BusinessRuleException("Mercadoria inválida");
        }
        if(lot.getUnits() < 0){
            throw new BusinessRuleException("Número de unidades inválido.");
        }

    }

    @Override
    public Lot saveLot(Lot lot) {
        validateLot(lot);
        return lotRepository.save(lot);
    }

    @Override
    public Lot updateLot(Lot lot) {
        if(!lotRepository.existsById(lot.getId())){
            throw new BusinessRuleException("Lot com id inválido.");
        }
        validateLot(lot);
        return lotRepository.save(lot);
    }

    @Override
    public Lot getLotById(Long id) {
        Optional<Lot> lot = lotRepository.findById(id);
        if(lot.isEmpty()){
            throw new BusinessRuleException("Lote com id inválido.");
        }
        return lot.get();
    }

    @Override
    public Page<Lot> getLot(Pageable pageable) {
        return lotRepository.findAll(pageable);
    }

    @Override
    public List<Lot> getLotsByPurchaseId(Long purchaseId) {
        return lotRepository.findLotsByPurchaseId(purchaseId);
    }

    @Override
    public List<Lot> getLotsByMerchandiseId(Long merchandiseId) {
        return lotRepository.findLotsByMerchandiseId(merchandiseId);
    }

    @Override
    public List<Lot> getLotsByPurchaseNoteNumber(String query) {
        return lotRepository.findLotsByLotNumber(query);
    }

    @Override
    public void deleteLot(Long id) {
        if(!lotRepository.existsById(id)) {
            throw new BusinessRuleException("Lote com id inválido.");
        }
        lotRepository.deleteById(id);
    }
}
