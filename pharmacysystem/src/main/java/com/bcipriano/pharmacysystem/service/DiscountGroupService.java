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

    public static void validateDiscountGroup(DiscountGroup discountGroup) {
        if (discountGroup.getName() == null) {
            throw new BusinessRuleException("O campo nome do grupo de desconto é obrigatório.");
        }
        if (discountGroup.getFinalDate() == null) {
            throw new BusinessRuleException("O campo data final do grupo de desconto é obrigatório.");
        }
        if (discountGroup.getPercentage() == null) {
            throw new BusinessRuleException("O campo percentual do grupo de desconto é obrigatório.");
        }
        if (discountGroup.getPercentage() <= 0 || discountGroup.getPercentage() > 100) {
            throw new BusinessRuleException("O percentual deve estár entre 0 e 100%.");
        }
        if (discountGroup.getMinimumUnits() == null) {
            throw new BusinessRuleException("O campo unidades mínimas do grupo de desconto é obrigatório.");
        }
        if (discountGroup.getMinimumUnits() < 0) {
            throw new BusinessRuleException("O valor para unidades mínimas deve ser maior que zero.");
        }
    }

    @Transactional
    public DiscountGroup saveDiscountGroup(DiscountGroup discountGroup) {
        if (discountGroupRepository.existsByName(discountGroup.getName())) {
            throw new BusinessRuleException("Já existe um grupo de desconto cadastrado com esse nome.");
        }
        validateDiscountGroup(discountGroup);
        return discountGroupRepository.save(discountGroup);
    }

    @Transactional
    public DiscountGroup updateDiscountGroup(DiscountGroup discountGroup) {
        if (!discountGroupRepository.existsById(discountGroup.getId())) {
            throw new NotFoundException("O id do grupo desconto que está tentado modificar não é válido.");
        }
        validateDiscountGroup(discountGroup);
        return discountGroupRepository.save(discountGroup);
    }

    @Transactional
    public void deleteDiscountGroup(Long id) {
        if (!discountGroupRepository.existsById(id)) {
            throw new NotFoundException("O id do grupo desconto que está tentado excluír não é válido.");
        }
        discountGroupRepository.deleteById(id);
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
            throw new IllegalArgumentException("Data de início inválida: " + startDate, dateTimeParseException);
        }
    }

    public Page<DiscountGroup> getDiscountGroupByFinalDate(String finalDate, Pageable pageable) {
        try {
            LocalDate startDateConverted = LocalDate.parse(finalDate);
            return discountGroupRepository.findByFinalDate(startDateConverted, pageable);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new IllegalArgumentException("Data de início inválida: " + finalDate, dateTimeParseException);
        }
    }

    public Optional<DiscountGroup> getDiscountGroupById(Long id) {
        Optional<DiscountGroup> discountGroup = discountGroupRepository.findById(id);
        if (discountGroup.isEmpty()) {
            throw new NotFoundException("O id do grupo desconto que está tentado buscar não é válido.");
        }
        return discountGroup;
    }


}
