package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.repository.DiscountGroupRepository;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.MerchandiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MerchandiseService {

    private MerchandiseRepository merchandiseRepository;

    private DiscountGroupRepository discountGroupRepository;

    @Autowired
    public MerchandiseService(MerchandiseRepository merchandiseRepository, DiscountGroupRepository discountGroupRepository) {
        this.merchandiseRepository = merchandiseRepository;
        this.discountGroupRepository = discountGroupRepository;
    }

    @Transactional
    public Merchandise saveMerchandise(Merchandise merchandise) {
        if (merchandiseRepository.existsByCode(merchandise.getCode())) {
            throw new BusinessRuleException("Já existe outra mercadoria cadastrada com esse código.");
        }
        if (merchandiseRepository.existsByName(merchandise.getName())) {
            throw new BusinessRuleException("Já existe outra mercadoria com esse nome cadastrada no sistema.");
        }
        return merchandiseRepository.save(merchandise);
    }

    @Transactional
    public Merchandise updateMerchandise(Merchandise merchandise) {
        if (!merchandiseRepository.existsById(merchandise.getId())) {
            throw new NotFoundException("A mercadoria que está tentando modificar não está cadastrada.");
        }

        Optional<Merchandise> merchandiseTest = merchandiseRepository.findByCode(merchandise.getCode());
        if (!merchandiseTest.isEmpty() && merchandiseTest.get().getId() != merchandise.getId()) {
            throw new BusinessRuleException("Erro ao atualizar a mercadoria. Já existe outra mercadoria com esse código.");
        }
        return merchandiseRepository.save(merchandise);
    }

    public Page<Merchandise> getMerchandise(Pageable pageable) {
        return merchandiseRepository.findAll(pageable);
    }

    public Page<Merchandise> findMerchandiseByQuery(String query, Pageable pageable) {
        return merchandiseRepository.findMerchandiseByQuery(query, pageable);
    }

    public Page<Merchandise> findMerchandiseByDiscountGroupId(Long id, Pageable pageable) {
        if (!discountGroupRepository.existsById(id)) {
            throw new NotFoundException("Id do grupo desconto inválido.");
        }
        return merchandiseRepository.findByDiscountGroupId(id, pageable);
    }

    public Page<Merchandise> findMerchandiseByDiscountGroupName(String name, Pageable pageable) {
        return merchandiseRepository.findByDiscountGroupName(name, pageable);
    }

    public Optional<Merchandise> findMerchandiseByCode(String code) {
        Optional<Merchandise> merchandise = merchandiseRepository.findByCode(code);
        if(merchandise.isEmpty()) {
            throw new NotFoundException("Nenhuma mercadoria encontrada com esse código de barras");
        }
        return merchandiseRepository.findByCode(code);
    }

    public Optional<Merchandise> getMerchandiseById(Long id) {
        Optional<Merchandise> merchandise = merchandiseRepository.findById(id);
        if (merchandise.isEmpty()) {
            throw new NotFoundException("Mercadoria com id inválido.");
        }
        return merchandise;
    }

    @Transactional
    public void deleteMerchandise(Long id) {
        if (!merchandiseRepository.existsById(id)) {
            throw new NotFoundException("Mercadoria com id inválido.");
        }
        merchandiseRepository.deleteById(id);
    }

}
