package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.model.repository.ReturnRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleItemRepository;
import com.bcipriano.pharmacysystem.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        if (rtn.getSaleItem() == null || !saleItemRepository.existsById(rtn.getSaleItem().getId())) {
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
            throw new BusinessRuleException("Registro de devolução com id inválido.");
        }
        validateReturn(rtn);
        return null;
    }

    @Override
    public Page<Return> getReturn(Pageable pageable) {
        return returnRepository.findAll(pageable);
    }

    @Override
    public Return getReturnBySaleItemId(Long saleId) {
        Optional<Return> returnOptional = returnRepository.findBySaleItemId(saleId);
        if(returnOptional.isEmpty()){
            throw new BusinessRuleException("Esse registro de devolução não está relacionado a nenhuma item vendido.");
        }
        return returnOptional.get();
    }

    @Override
    public Return getReturnById(Long id) {
        Optional<Return> rtn = returnRepository.findById(id);
        if (rtn.isEmpty()) {
            throw new BusinessRuleException("Registro de devolução com id inválido.");
        }
        return rtn.get();
    }

    @Override
    public void deleteReturn(Long id) {
        if (!returnRepository.existsById(id)) {
            throw new BusinessRuleException("Registro de devolução com id inválido.");
        }
        returnRepository.deleteById(id);
    }
}
