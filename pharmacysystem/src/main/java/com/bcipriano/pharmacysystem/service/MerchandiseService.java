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

    public static void validateMerchandise(Merchandise merchandise) {

        if (merchandise.getName() == null || merchandise.getName().trim().equals("")) {
            throw new BusinessRuleException("O campo nome da mercadoria é obrigatório.");
        }
        if (merchandise.getCode() == null) {
            throw new BusinessRuleException("O campo código de barras é obrigatório.");
        }
        if (merchandise.getCode().length() != 13) {
            throw new BusinessRuleException("O codigo de barras da mercadoria inválido.(Verifique os 13 digitos)");
        }
        if (merchandise.getDepartment() == null) {
            throw new BusinessRuleException("Departamento inválido.");
        }
        if (merchandise.getBrand() == null || merchandise.getBrand().trim().equals("")) {
            throw new BusinessRuleException("O campo marca é obrigatório.");
        }
        if (merchandise.getStripe() == null) {
            throw new BusinessRuleException("Tarja inválida.");
        }
        if (merchandise.getStorageTemperature() == null) {
            throw new BusinessRuleException("Temperatura de armazenamento inválida.");
        }
        if (merchandise.getMinimumStock() < 0) {
            throw new BusinessRuleException("Valor inválido para o estoque mínimo.");
        }
        if (merchandise.getMaximumStock() < 0) {
            throw new BusinessRuleException("Valor inválido para o estoque máximo.");
        }
        if (merchandise.getFullPrice() < 0) {
            throw new BusinessRuleException("Valor inválido para o preço inteiro.");
        }
        if (merchandise.getComission() == null || merchandise.getComission() < 0 || merchandise.getComission() > 100) {
            throw new BusinessRuleException("Valor inválido para o percentual de comissão.");
        }
        if (merchandise.getPmc() < 0) {
            throw new BusinessRuleException("Valor inválido para o preço máximo permitido ao consumidor.");
        }

    }

    @Transactional
    public Merchandise saveMerchandise(Merchandise merchandise) {
        if (merchandiseRepository.existsByCode(merchandise.getCode())) {
            throw new BusinessRuleException("Já existe outra mercadoria cadastrada com esse código.");
        }
        if (merchandiseRepository.existsByName(merchandise.getName())) {
            throw new BusinessRuleException("Já existe outra mercadoria com esse nome cadastrada no sistema.");
        }
        validateMerchandise(merchandise);
        return merchandiseRepository.save(merchandise);
    }

    @Transactional
    public Merchandise updateMerchandise(Merchandise merchandise) {
        if (!merchandiseRepository.existsById(merchandise.getId())) {
            throw new BusinessRuleException("A mercadoria que está tentando modificar não está cadastrada.");
        }

        validateMerchandise(merchandise);

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
        return merchandiseRepository.findByCode(code);
    }

    public Optional<Merchandise> getMerchandiseById(Long id) {
        Optional<Merchandise> merchandise = merchandiseRepository.findById(id);
        if (merchandise.isEmpty()) {
            throw new BusinessRuleException("Mercadoria com id inválido.");
        }
        return merchandise;
    }

    @Transactional
    public void deleteMerchandise(Long id) {
        if (!merchandiseRepository.existsById(id)) {
            throw new BusinessRuleException("Mercadoria com id inválido.");
        }
        merchandiseRepository.deleteById(id);
    }

}
