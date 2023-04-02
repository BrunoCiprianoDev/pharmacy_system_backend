package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.model.entity.Loss;

import java.util.List;

public interface LossService {

    void validateLoss(Loss loss);

    Loss saveLoss(Loss loss);

    Loss updateLoss(Loss loss);

    List<Loss> getLoss();

    Loss getLossById(Long id);

    Loss getLossByQuery(String query);

    void deleteLoss(Long id);


}
