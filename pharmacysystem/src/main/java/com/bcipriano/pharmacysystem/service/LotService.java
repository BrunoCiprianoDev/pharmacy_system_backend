package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Lot;

import java.util.List;

public interface LotService {

    void validateLot(Lot lot);

    Lot saveLot(Lot lot);

    Lot updateLot(Lot lot);

    Lot getLotById(Long id);

    List<Lot> getLot();

    List<Lot> getLotsByPurchaseId(Long purchaseId);

    List<Lot> getLotsByMerchandiseId(Long merchandiseId);

    List<Lot> getLotsByPurchaseNoteNumber(String query);

    void deleteLot(Long id);

}
