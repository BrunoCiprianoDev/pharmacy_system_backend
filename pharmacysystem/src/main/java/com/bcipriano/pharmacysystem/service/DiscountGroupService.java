package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.repository.DiscountGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class DiscountGroupService {

    private DiscountGroupRepository discountGroupRepository;

    @Autowired
    public DiscountGroupService(DiscountGroupRepository discountGroupRepository) {
        this.discountGroupRepository = discountGroupRepository;
    }

    public Page<DiscountGroup> getDiscountGroup(Pageable pageable) {
        return discountGroupRepository.findAll(pageable);
    }

    public Page<DiscountGroup> getDiscountGroupByQuery(String query, Pageable pageable) {
        return discountGroupRepository.findDiscountGroupByQuery(query, pageable);
    }

    public Page<DiscountGroup> getDiscountGroupByStartDate(String startDate, Pageable pageable) {
        try {
            LocalDate startDateConverted = LocalDate.parse(startDate);
            return discountGroupRepository.findByStartDate(startDateConverted, pageable);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new BusinessRuleException("Data de início inválida: " + startDate);
        }
    }

    public Page<DiscountGroup> getDiscountGroupByFinalDate(String finalDate, Pageable pageable) {
        try {
            LocalDate startDateConverted = LocalDate.parse(finalDate);
            return discountGroupRepository.findByFinalDate(startDateConverted, pageable);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new BusinessRuleException("Data final inválida: " + finalDate);
        }
    }

    public Optional<DiscountGroup> getDiscountGroupById(Long id) {
        Optional<DiscountGroup> discountGroup = discountGroupRepository.findById(id);
        if (discountGroup.isEmpty()) {
            throw new NotFoundException("O id do grupo desconto que está tentado buscar não é válido.");
        }
        return discountGroup;
    }

    @Transactional
    public DiscountGroup saveDiscountGroup(DiscountGroup discountGroup) {
        if (discountGroupRepository.existsByName(discountGroup.getName())) {
            throw new BusinessRuleException("Já existe um grupo de desconto cadastrado com esse nome.");
        }
        return discountGroupRepository.save(discountGroup);
    }

    @Transactional
    public DiscountGroup updateDiscountGroup(DiscountGroup discountGroup) {
        if (!discountGroupRepository.existsById(discountGroup.getId())) {
            throw new NotFoundException("O id do grupo desconto que está tentado modificar não é válido.");
        }
        return discountGroupRepository.save(discountGroup);
    }

    @Transactional
    public void deleteDiscountGroup(Long id) {
        if (!discountGroupRepository.existsById(id)) {
            throw new NotFoundException("O id do grupo desconto que está tentado excluír não é válido.");
        }
        discountGroupRepository.deleteById(id);
    }


}
