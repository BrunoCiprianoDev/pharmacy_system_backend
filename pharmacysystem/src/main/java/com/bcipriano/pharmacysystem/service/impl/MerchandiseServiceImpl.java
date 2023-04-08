package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.repository.DiscountGroupRepository;
import com.bcipriano.pharmacysystem.model.repository.MerchandiseRepository;
import com.bcipriano.pharmacysystem.service.MerchandiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerchandiseServiceImpl implements MerchandiseService {

    private MerchandiseRepository merchandiseRepository;

    @Autowired
    public MerchandiseServiceImpl(MerchandiseRepository merchandiseRepository){
        this.merchandiseRepository = merchandiseRepository;
    }

    @Override
    public void validateMerchandise(Merchandise merchandise) {
        if(merchandise.getName() == null || merchandise.getName().trim().equals("")){
            throw new BusinessRuleException("O campo nome é obrigatório.");
        }
        if(merchandise.getCode() == null){
            throw new BusinessRuleException("O campo código de barras é obrigatório.");
        }
        if(merchandise.getCode().length() != 13){
            throw new BusinessRuleException("O codigo de barras inválido.(Verifique os 13 digitos)");
        }
        if(merchandise.getDepartment() == null || merchandise.getName().trim().equals("")){
            throw new BusinessRuleException("Departamento inválido.");
        }
        if(merchandise.getBrand() == null || merchandise.getBrand().trim().equals("")){
            throw new BusinessRuleException("O campo marca é obrigatório.");
        }
        if(merchandise.getStripe() == null){
            throw new BusinessRuleException("Tarja inválida.");
        }
        if(merchandise.getMinimumStock() < 0){
            throw new BusinessRuleException("Valor inválido para o estoque mínimo.");
        }
        if(merchandise.getCurrentStock() < 0){
            throw new BusinessRuleException("Valor inválido para o estoque atual.");
        }
        if(merchandise.getMaximumStock() < 0){
            throw new BusinessRuleException("Valor inválido para o estoque máximo.");
        }
        if(merchandise.getFullPrice() < 0) {
            throw new BusinessRuleException("Valor inválido para o preço inteiro.");
        }
        if(merchandise.getComission() == null || merchandise.getComission() < 0 || merchandise.getComission() > 100) {
            throw new BusinessRuleException("Valor inválido para o percentual de comissão.");
        }
        if(merchandise.getPmc() < 0) {
            throw new BusinessRuleException("Valor inválido para o preço máximo permitido ao consumidor.");
        }
    }

    @Override
    public Merchandise saveMerchandise(Merchandise merchandise) {
        if(merchandiseRepository.existsByCode(merchandise.getCode())){
            throw new BusinessRuleException("Já existe outra mercadoria cadastrada com esse código.");
        }
        if(merchandiseRepository.existsByName(merchandise.getName())){
            throw new BusinessRuleException("Já existe outra mercadoria com esse nome cadastrada no sistema.");
        }
        validateMerchandise(merchandise);
        return merchandiseRepository.save(merchandise);
    }

    @Override
    public Merchandise updateMerchandise(Merchandise merchandise) {
        if(!merchandiseRepository.existsById(merchandise.getId())){
            throw new BusinessRuleException("A mercadoria que está tentando modificar não está cadastrada.");
        }
        validateMerchandise(merchandise);
        return merchandiseRepository.save(merchandise);
    }

    @Override
    public Page<Merchandise> getMerchandise(Pageable pageable) {
        return merchandiseRepository.findAll(pageable);
    }

    @Override
    public Merchandise getMerchandiseById(Long id) {
        Optional<Merchandise> merchandise = merchandiseRepository.findById(id);
        if(merchandise.isEmpty()){
            throw new BusinessRuleException("Mercadoria com id inválido.");
        }
        return merchandise.get();
    }

    @Override
    public List<Merchandise> findMerchandiseByQuery(String query) {
        return merchandiseRepository.findMerchandiseByQuery(query);
    }

    @Override
    public void deleteMerchandise(Long id) {
        if(!merchandiseRepository.existsById(id)){
            throw new BusinessRuleException("Mercadoria com id inválido.");
        }
        merchandiseRepository.deleteById(id);
    }
}
