package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LossRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LossService{

    LossRepository lossRepository;

    LotRepository lotRepository;

    EmployeeRepository employeeRepository;

    public LossService(LossRepository lossRepository, LotRepository lotRepository, EmployeeRepository employeeRepository) {
        this.lossRepository = lossRepository;
        this.lotRepository = lotRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Loss saveLoss(Loss loss) {

        Integer currentUnits = loss.getLot().getUnits() - loss.getUnits();
        if(currentUnits < 0) {
            throw new BusinessRuleException("Número de itens perdidos inválido.");
        }

        lotRepository.subtractUnitsFromLot(loss.getLot().getId(), loss.getUnits());
        return lossRepository.save(loss);
    }

    @Transactional
    public Loss updateLoss(Loss loss) {

        if (!lossRepository.existsById(loss.getId())) {
            throw new NotFoundException("Registro de perda com id inválido.");
        }

        Loss currentLoss = lossRepository.findLossById(loss.getId()).get();

        Integer diff = currentLoss.getUnits() - loss.getUnits();

        if(diff > 0) {
            lotRepository.addUnitsFromLot(loss.getLot().getId(), diff);
        } else {
            lotRepository.subtractUnitsFromLot(loss.getLot().getId(), -diff);
        }
        return lossRepository.save(loss);
    }

    public Page<Loss> getLoss(Pageable pageable) {
        return lossRepository.findAll(pageable);
    }

    public Loss getLossById(Long id) {
        Optional<Loss> loss = lossRepository.findLossById(id);
        if (loss.isEmpty()) {
            throw new NotFoundException("Registro de perda com id inválido.");
        }
        return loss.get();
    }

    public Page<Loss> getLossByQueryLotNumber(String query, Pageable pageable) {
        return lossRepository.findLossByLotNumber(query, pageable);
    }

    public Page<Loss> getLossByLotid(Long lotId, Pageable pageable){
        if(!lossRepository.existsById(lotId)) {
            throw new NotFoundException("Id do Lot não encontrado.");
        }
        return lossRepository.findLossByLotId(lotId, pageable);
    }

    @Transactional
    public void deleteLoss(Long id) {
        Optional<Loss> lossOptional = lossRepository.findLossById(id);
        if(lossOptional.isEmpty()) {
            throw new BusinessRuleException("Registro de perda com id inválido.");
        }
        Loss lossLoaded = lossOptional.get();
        if(lotRepository.existsById(lossLoaded.getLot().getId())) {
            lotRepository.addUnitsFromLot( lossLoaded.getLot().getId(), lossLoaded.getUnits());
        }
        lossRepository.deleteById(id);
    }
}