package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.model.repository.ReturnRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReturnService{

    ReturnRepository returnRepository;

    SaleItemRepository saleItemRepository;

    @Autowired
    public ReturnService(ReturnRepository returnRepository, SaleItemRepository saleItemRepository) {
        this.returnRepository = returnRepository;
        this.saleItemRepository = saleItemRepository;
    }

    public void validateReturn(Return rtn) {

        if (rtn.getDescription() == null || rtn.getDescription().trim().equals("")) {
            throw new BusinessRuleException("Adicione uma descrição para a devolução.");
        }

        if (rtn.getSaleItem() == null || !saleItemRepository.existsById(rtn.getSaleItem().getId())) {
            throw new BusinessRuleException("Essa devolução não está relacionada a nenhum item vendido.");
        }

    }

    public Return saveReturn(Return rtn) {
        validateReturn(rtn);
        return returnRepository.save(rtn);
    }

    public Return updateReturn(Return rtn) {
        if (!returnRepository.existsById(rtn.getId())) {
            throw new BusinessRuleException("Registro de devolução com id inválido.");
        }
        validateReturn(rtn);
        return null;
    }

    public Page<Return> getReturn(Pageable pageable) {
        return returnRepository.findAll(pageable);
    }

    public Return getReturnBySaleItemId(Long saleId) {
        Optional<Return> returnOptional = returnRepository.findBySaleItemId(saleId);
        if(returnOptional.isEmpty()){
            throw new BusinessRuleException("Esse registro de devolução não está relacionado a nenhuma item vendido.");
        }
        return returnOptional.get();
    }

    public Return getReturnById(Long id) {
        Optional<Return> rtn = returnRepository.findById(id);
        if (rtn.isEmpty()) {
            throw new BusinessRuleException("Registro de devolução com id inválido.");
        }
        return rtn.get();
    }

    public void deleteReturn(Long id) {
        if (!returnRepository.existsById(id)) {
            throw new BusinessRuleException("Registro de devolução com id inválido.");
        }
        returnRepository.deleteById(id);
    }
}