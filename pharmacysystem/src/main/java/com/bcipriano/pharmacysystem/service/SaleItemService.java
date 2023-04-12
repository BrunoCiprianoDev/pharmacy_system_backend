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

    List<SaleItem> saveSaleItemList(List<SaleItem> saleItemList);

    SaleItem updateSaleItem(SaleItem saleItem);

    List<SaleItem> getSaleItems();

    List<SaleItem> getSaleItemBySaleId(Long saleId);

    SaleItem getSaleItemById(Long id);

    List<SaleItem> getSaleItemByLotId(Long lotId);

    void deleteSaleItem(Long id);

    void deleteBySaleId(Long saleId);

}
