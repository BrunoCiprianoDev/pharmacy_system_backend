package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Lot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LotService {

    void validateLot(Lot lot);

    Lot saveLot(Lot lot);

    Lot updateLot(Lot lot);

    Lot getLotById(Long id);

    Page<Lot> getLot(Pageable pageable);

    List<Lot> getLotsByPurchaseId(Long purchaseId);

    List<Lot> getLotsByMerchandiseId(Long merchandiseId);

    List<Lot> getLotsByPurchaseNoteNumber(String query);

    void deleteLot(Long id);

}
