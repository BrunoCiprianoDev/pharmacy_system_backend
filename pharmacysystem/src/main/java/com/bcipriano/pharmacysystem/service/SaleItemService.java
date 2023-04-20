package com.bcipriano.pharmacysystem.service;

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
public class SaleItemService {

    private SaleItemRepository saleItemRepository;

    private SaleRepository saleRepository;
    private LotRepository lotRepository;

    @Autowired
    public SaleItemService(SaleItemRepository saleItemRepository,
                           SaleRepository saleRepository,
                           LotRepository lotRepository
    ) {
        this.saleItemRepository = saleItemRepository;
        this.saleRepository = saleRepository;
        this.lotRepository = lotRepository;
    }


    public static void validateSaleItem(SaleItem saleItem, LotRepository lotRepository) {

        if (saleItem.getUnits() < 0) {
            throw new BusinessRuleException("Valor inválido para o número de unidades");
        }
        if (saleItem.getSellPrice() < 0) {
            throw new BusinessRuleException("Valor inválido para o valor de venda.");
        }
        if (saleItem.getLot() == null || !lotRepository.existsById(saleItem.getLot().getId())) {
            throw new BusinessRuleException("Lote inválido.");
        }

    }

    @Transactional
    public SaleItem saveSaleItem(SaleItem saleItem) {
        if (!saleRepository.existsById(saleItem.getSale().getId())) {
            throw new BusinessRuleException("Esse item não pertence a um venda cadastrada.");
        }
        if (saleItem.getId() != null && saleItemRepository.existsById(saleItem.getId())) {
            throw new BusinessRuleException("Já existe item venda cadastrada com esse id");
        }
        SaleItemService.validateSaleItem(saleItem, lotRepository);
        lotRepository.subtractUnitsFromLot(saleItem.getLot().getId(), saleItem.getUnits());
        return saleItemRepository.save(saleItem);
    }

    @Transactional
    public SaleItem updateSaleItem(SaleItem saleItem) {
        if (!saleRepository.existsById(saleItem.getSale().getId())) {
            throw new BusinessRuleException("Esse item não pertence a um venda cadastrada.");
        }
        if (!saleItemRepository.existsById(saleItem.getId())) {
            throw new BusinessRuleException("O item que está tentando modificar não está cadastrado no sistema");
        }
        SaleItemService.validateSaleItem(saleItem, lotRepository);
        return saleItemRepository.save(saleItem);
    }

    public List<SaleItem> getSaleItems() {
        return saleItemRepository.findAll();
    }

    public List<SaleItem> getSaleItemBySaleId(Long saleId) {
        if (!saleRepository.existsById(saleId)) {
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        return saleItemRepository.findSaleItemsBySaleId(saleId);
    }

    public Optional<SaleItem> getSaleItemById(Long id) {
        Optional<SaleItem> saleItem = saleItemRepository.findSaleItemById(id);
        if (saleItem.isEmpty()) {
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        return saleItem;
    }

    public List<SaleItem> getSaleItemByLotId(Long lotId) {
        return saleItemRepository.findSaleItemByLotId(lotId);
    }

    public void deleteSaleItem(SaleItem saleItem) {
        if (!saleItemRepository.existsById(saleItem.getId())) {
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        if(!lotRepository.existsById(saleItem.getLot().getId())) {
            throw new BusinessRuleException("Lot com Id inválido.");
        }

        lotRepository.addUnitsFromLot(saleItem.getLot().getId(), saleItem.getUnits());

        saleItemRepository.deleteById(saleItem.getId());
    }

}