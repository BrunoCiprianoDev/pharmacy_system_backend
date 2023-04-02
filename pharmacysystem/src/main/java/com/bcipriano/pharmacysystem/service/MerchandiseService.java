package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Merchandise;

import java.util.List;

public interface MerchandiseService {

    void validateMerchandise(Merchandise merchandise);

    Merchandise saveMerchandise(Merchandise merchandise);

    Merchandise updateMerchandise(Merchandise merchandise);

    List<Merchandise> getMerchandise();

    Merchandise getMerchandiseById(Long id);

    List<Merchandise> findMerchandiseByQuery(String query);

    void deleteMerchandise(Long id);

}
