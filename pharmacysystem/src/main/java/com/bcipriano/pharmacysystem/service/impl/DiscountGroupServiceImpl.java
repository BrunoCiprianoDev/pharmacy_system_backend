package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.repository.DiscountGroupRepository;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountGroupServiceImpl implements DiscountGroupService {

    private DiscountGroupRepository discountGroupRepository;

    @Autowired
    public DiscountGroupServiceImpl(DiscountGroupRepository discountGroupRepository){
        this.discountGroupRepository = discountGroupRepository;
    }

    @Override
    public void validateDiscountGroup(DiscountGroup discountGroup) {

        if(discountGroup.getName() == null) {
            throw new BusinessRuleException("O campo nome é obrigatório.");
        }
        if(discountGroup.getStartDate() == null ){
            throw new BusinessRuleException("O campo data inicial é obrigatório.");
        }
        if(discountGroup.getFinalDate() == null){
            throw new BusinessRuleException("O campo data final é obrigatório.");
        }
        if(discountGroup.getPercentage() <= 0 || discountGroup.getPercentage() > 100) {
            throw new BusinessRuleException("O percentual deve estár entre 0 e 100%.");
        }
        if(discountGroup.getMinimumUnits() < 0){
            throw new BusinessRuleException("O valor para unidades mínimas deve ser maior que zero.");
        }
    }

    @Override
    @Transactional
    public DiscountGroup saveDiscountGroup(DiscountGroup discountGroup) {
        boolean exists = discountGroupRepository.existsByName(discountGroup.getName());
        if(exists){
            throw new BusinessRuleException("Já existe um grupo de desconto com esse nome.");
        }
        validateDiscountGroup(discountGroup);
        return discountGroupRepository.save(discountGroup);
    }

    @Override
    @Transactional
    public DiscountGroup updateDiscountGroup(DiscountGroup discountGroup) {
        if(!discountGroupRepository.existsById(discountGroup.getId())) {
            throw new BusinessRuleException("O grupo desconto que está tentado modificar não está cadastrado no sistema.");
        }
        validateDiscountGroup(discountGroup);
        return discountGroupRepository.save(discountGroup);
    }

    @Override
    public List<DiscountGroup> getDiscountGroup() {
        return discountGroupRepository.findAll();
    }

    @Override
    public DiscountGroup getDiscountGroupById(Long id) {
        Optional<DiscountGroup> discountGroup = discountGroupRepository.findById(id);
        if(discountGroup.isEmpty()){
            throw new BusinessRuleException("Grupo desconto com id inválido!");
        }
        return discountGroup.get();
    }

    @Override
    public List<DiscountGroup> getDiscountGroupByQuery(String query) {
        return discountGroupRepository.findDiscountGroupByQuery(query);
    }

    @Override
    public List<DiscountGroup> getDiscountGroupByStartDate(LocalDate startDate) {
        return discountGroupRepository.findByStartDate(startDate);
    }

    @Override
    public List<DiscountGroup> getDiscountGroupByFinalDate(LocalDate finalDate) {
        return discountGroupRepository.findByFinalDate(finalDate);
    }

    @Override
    @Transactional
    public void deleteDiscountGroup(Long id) {
        boolean exists = discountGroupRepository.existsById(id);
        if(!exists){
            throw new BusinessRuleException("Grupo desconto com id inválido!");
        }
        discountGroupRepository.deleteById(id);
    }
}
