package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleItemRepository;
import com.bcipriano.pharmacysystem.model.repository.SaleRepository;

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

    @Transactional
    public SaleItem saveSaleItem(SaleItem saleItem) {
        if (!saleRepository.existsById(saleItem.getSale().getId())) {
            throw new NotFoundException("Venda com ID inválido.");
        }
        if (saleItem.getUnits() < 0 || saleItem.getUnits() == null) {
            throw new BusinessRuleException("Valor inválido para o número de unidades");
        }
        Integer availableUnits = saleItem.getLot().getUnits() - saleItem.getUnits();
        if (availableUnits < 0) {
            throw new BusinessRuleException("Número de itens inválido para o lote nº " + saleItem.getLot().getNumber() + " de " + saleItem.getLot().getMerchandise().getName());
        }

        lotRepository.subtractUnitsFromLot(saleItem.getLot().getId(), saleItem.getUnits());

        Double sellPrice = 0D;
        if (saleItem.getLot().getMerchandise().getDiscountGroup() != null) {
            sellPrice = priceCalculator(saleItem.getLot().getMerchandise().getFullPrice(), saleItem.getLot().getMerchandise().getDiscountGroup().getPercentage());
            Double unitsCast = saleItem.getUnits().doubleValue();
            sellPrice = sellPrice * unitsCast;
        } else {
            Double unitsCast = saleItem.getUnits().doubleValue();
            sellPrice = (saleItem.getLot().getMerchandise().getFullPrice() * unitsCast);
        }
        saleItem.setSellPrice(sellPrice);
        return saleItemRepository.save(saleItem);
    }

    @Transactional
    public SaleItem updateSaleItem(SaleItem saleItem) {
        Optional<SaleItem> saleItemLoaded = saleItemRepository.findById(saleItem.getId());
        if(saleItemLoaded.isEmpty()){
            throw new BusinessRuleException("Item venda com ID inválido.");
        }
        SaleItem saleItemUpdated = saleItemLoaded.get();
        saleItemUpdated.setUnits(saleItem.getUnits());
        return saleItemRepository.save(saleItemUpdated);
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


    public Double priceCalculator(Double merchandisePrice, Double discountPercentage) {
        Double result = merchandisePrice;
        result = merchandisePrice - (merchandisePrice * (discountPercentage / 100));
        return result;
    }

    public void deleteSaleItem(SaleItem saleItem) {
        if (!saleItemRepository.existsById(saleItem.getId())) {
            throw new BusinessRuleException("Item venda com id inválido.");
        }
        if (!lotRepository.existsById(saleItem.getLot().getId())) {
            throw new BusinessRuleException("Lot com Id inválido.");
        }

        lotRepository.addUnitsFromLot(saleItem.getLot().getId(), saleItem.getUnits());

        saleItemRepository.deleteById(saleItem.getId());
    }

}