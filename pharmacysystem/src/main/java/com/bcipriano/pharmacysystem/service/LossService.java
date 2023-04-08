package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Loss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LossService {

    void validateLoss(Loss loss);

    Loss saveLoss(Loss loss);

    Loss updateLoss(Loss loss);

    Page<Loss> getLoss(Pageable pageable);

    Loss getLossById(Long id);

    List<Loss> getLossByQuery(String query);

    void deleteLoss(Long id);


}
