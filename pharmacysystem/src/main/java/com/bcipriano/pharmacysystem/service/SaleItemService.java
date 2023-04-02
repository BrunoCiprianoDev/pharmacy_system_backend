package com.bcipriano.pharmacysystem.service;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.model.repository.LotRepository;

import java.util.List;

public interface SaleItemService {

    static void validateSaleItem(SaleItem saleItem, LotRepository lotRepository) {

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

    SaleItem saveSaleItem(SaleItem saleItem);

    SaleItem updateSaleItem(SaleItem saleItem);

    List<SaleItem> getSaleItemBySaleId(Long saleId);

    List<SaleItem> getSaleItemByLotId(Long lotId);

    void deleteSaleItem(Long id);

}
