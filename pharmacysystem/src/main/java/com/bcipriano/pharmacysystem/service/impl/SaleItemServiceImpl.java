package com.bcipriano.pharmacysystem.service.impl;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleItemRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleRepository;
import com.bcipriano.pharmacysystem.service.SaleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SaleItemServiceImpl implements SaleItemService {

    private SaleItemRepository saleItemRepository;

    private SaleRepository saleRepository;
    private LotRepository lotRepository;

    @Autowired
    public SaleItemServiceImpl(SaleItemRepository saleItemRepository,
                               SaleRepository saleRepository,
                               LotRepository lotRepository
    ) {
        this.saleItemRepository = saleItemRepository;
        this.saleRepository = saleRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    @Transactional
    public SaleItem saveSaleItem(SaleItem saleItem) {
        if(!saleRepository.existsById(saleItem.getSale().getId())){
            throw new BusinessRuleException("Esse item não pertence a um venda cadastrada.");
        }
        if (saleItem.getId() != null && saleItemRepository.existsById(saleItem.getId())) {
            throw new BusinessRuleException("Já existe item venda cadastrada com esse id");
        }
        SaleItemService.validateSaleItem(saleItem, lotRepository);
        return saleItemRepository.save(saleItem);
    }

    @Override
    @Transactional
    public List<SaleItem> saveSaleItemList(List<SaleItem> saleItemList) {
        for(SaleItem saleItem : saleItemList) {
            if(!saleRepository.existsById(saleItem.getSale().getId())){
                throw new BusinessRuleException("Esse item não pertence a um venda cadastrada.");
            }
            if (saleItemRepository.existsById(saleItem.getId())) {
                throw new BusinessRuleException("Já existe item venda cadastrada com esse id");
            }
            SaleItemService.validateSaleItem(saleItem, lotRepository);
        }
        return saleItemRepository.saveAll(saleItemList);
    }

    @Override
    @Transactional
    public SaleItem updateSaleItem(SaleItem saleItem) {
        if(!saleRepository.existsById(saleItem.getSale().getId())){
            throw new BusinessRuleException("Esse item não pertence a um venda cadastrada.");
        }
        if (!saleItemRepository.existsById(saleItem.getId())) {
            throw new BusinessRuleException("O item que está tentando modificar não está cadastrado no sistema");
        }
        SaleItemService.validateSaleItem(saleItem, lotRepository);
        return saleItemRepository.save(saleItem);
    }

    @Override
    public List<SaleItem> getSaleItems() {
        return saleItemRepository.findAll();
    }

    @Override
    public List<SaleItem> getSaleItemBySaleId(Long saleId) {
        if(!saleRepository.existsById(saleId)){
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        return saleItemRepository.findSaleItemsBySaleId(saleId);
    }

    @Override
    public SaleItem getSaleItemById(Long id){
        Optional<SaleItem> saleItem = saleItemRepository.findSaleItemById(id);
        if(saleItem.isEmpty()){
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        return saleItem.get();
    }

    @Override
    public List<SaleItem> getSaleItemByLotId(Long lotId) {
        return saleItemRepository.findSaleItemByLotId(lotId);
    }

    @Override
    public void deleteSaleItem(Long id) {
        if(!saleItemRepository.existsById(id)){
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        saleItemRepository.deleteById(id);
    }
}
