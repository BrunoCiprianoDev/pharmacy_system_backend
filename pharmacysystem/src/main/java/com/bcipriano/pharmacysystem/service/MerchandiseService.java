package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MerchandiseService {

    void validateMerchandise(Merchandise merchandise);

    Merchandise saveMerchandise(Merchandise merchandise);

    Merchandise updateMerchandise(Merchandise merchandise);

    Page<Merchandise> getMerchandise(Pageable pageable);

    Merchandise getMerchandiseById(Long id);

    List<Merchandise> findMerchandiseByQuery(String query);

    void deleteMerchandise(Long id);

}
