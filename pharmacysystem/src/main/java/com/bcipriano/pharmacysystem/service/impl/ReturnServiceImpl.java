package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.model.repository.ReturnRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleItemRepository;
import com.bcipriano.pharmacysystem.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnServiceImpl implements ReturnService {

    ReturnRepository returnRepository;

    SaleItemRepository saleItemRepository;

    @Autowired
    public ReturnServiceImpl(ReturnRepository returnRepository, SaleItemRepository saleItemRepository) {
        this.returnRepository = returnRepository;
        this.saleItemRepository = saleItemRepository;
    }

    @Override
    public void validateReturn(Return rtn) {

        if (rtn.getDescription() == null || rtn.getDescription().trim().equals("")) {
            throw new BusinessRuleException("Adicione uma descrição para a devolução.");
        }

        if (!saleItemRepository.existsById(rtn.getSaleItem().getId())) {
            throw new BusinessRuleException("Essa devolução não está relacionada a nenhum item vendido.");
        }

    }

    @Override
    public Return saveReturn(Return rtn) {
        validateReturn(rtn);
        return returnRepository.save(rtn);
    }

    @Override
    public Return updateReturn(Return rtn) {
        if (!returnRepository.existsById(rtn.getId())) {
            throw new InvalidIdException();
        }
        validateReturn(rtn);
        return null;
    }

    @Override
    public List<Return> getAllReturn() {
        return returnRepository.findAll();
    }

    @Override
    public List<Return> getReturnBySaleItemId(Long saleId) {
        if (!saleItemRepository.existsById(saleId)) {
            throw new InvalidIdException();
        }
        return returnRepository.findBySaleItemId(saleId);
    }

    @Override
    public Return getReturnById(Long id) {
        Optional<Return> rtn = returnRepository.findById(id);
        if (rtn.isEmpty()) {
            throw new InvalidIdException();
        }
        return rtn.get();
    }

    @Override
    public void deleteReturn(Long id) {
        if (!returnRepository.existsById(id)) {
            throw new InvalidIdException();
        }
        returnRepository.deleteById(id);
    }
}
