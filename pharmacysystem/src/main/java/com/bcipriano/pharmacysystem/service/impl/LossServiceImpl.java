package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.repository.EmployeeRepository;
import com.bcipriano.pharmacysystem.model.repository.LossRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.service.LossService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LossServiceImpl implements LossService {

    LossRepository lossRepository;

    LotRepository lotRepository;

    EmployeeRepository employeeRepository;

    public LossServiceImpl(LossRepository lossRepository, LotRepository lotRepository, EmployeeRepository employeeRepository) {
        this.lossRepository = lossRepository;
        this.lotRepository = lotRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void validateLoss(Loss loss) {
        if (loss.getRegisterDate() == null) {
            throw new BusinessRuleException("Data de registro inválida.");
        }
        if (loss.getUnits() < 0) {
            throw new BusinessRuleException("Número de unidades inválido.");
        }
        if (loss.getLot() == null || !lotRepository.existsById(loss.getLot().getId())) {
            throw new BusinessRuleException("Lote inválido.");
        }
        if (loss.getEmployee() == null || !employeeRepository.existsById(loss.getEmployee().getId())) {
            throw new BusinessRuleException("Funcionário inválido.");
        }
    }

    @Override
    public Loss saveLoss(Loss loss) {
        validateLoss(loss);
        return lossRepository.save(loss);
    }

    @Override
    public Loss updateLoss(Loss loss) {
        if (!lossRepository.existsById(loss.getId())) {
            throw new BusinessRuleException("Registro de perda com id inválido.");
        }
        validateLoss(loss);
        return null;
    }

    @Override
    public Page<Loss> getLoss(Pageable pageable) {
        return lossRepository.findAll(pageable);
    }

    @Override
    public Loss getLossById(Long id) {
        Optional<Loss> loss = lossRepository.findLossById(id);
        if (loss.isEmpty()) {
            throw new BusinessRuleException("Registro de perda com id inválido.");
        }
        return loss.get();
    }

    @Override
    public List<Loss> getLossByQuery(String query) {
        return lossRepository.findLossByLotNumber(query);
    }

    @Override
    public void deleteLoss(Long id) {
        if(!lossRepository.existsById(id)) {
            throw new BusinessRuleException("Registro de perda com id inválido.");
        }
        lossRepository.deleteById(id);
    }
}
