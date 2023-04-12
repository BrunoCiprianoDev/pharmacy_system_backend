package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.SaleItemDTO;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.service.LotService;
import com.bcipriano.pharmacysystem.service.SaleItemService;
import com.bcipriano.pharmacysystem.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saleItems")
@RequiredArgsConstructor
public class SaleItemController {

    private final SaleItemService saleItemService;

    @GetMapping
    public ResponseEntity get(){
        List<SaleItem> saleItemList = saleItemService.getSaleItems();
        return ResponseEntity.ok(saleItemList);
    }

    @GetMapping("/sale")
    public ResponseEntity get(@RequestParam("saleId") Long saleId) {
        List<SaleItem> saleItemList =  saleItemService.getSaleItemBySaleId(saleId);
        return ResponseEntity.ok(saleItemList);
    }

    public static SaleItem converter(SaleItemDTO saleItemDTO, SaleService saleService, LotService lotService){
        ModelMapper modelMapper = new ModelMapper();
        SaleItem saleItem = modelMapper.map(saleItemDTO, SaleItem.class);
        if(saleItemDTO.getSaleId() != null ){
            saleItem.setSale(saleService.getSaleById(saleItemDTO.getSaleId()));
        }
        if(saleItemDTO.getLotId() != null ){
            saleItem.setLot(lotService.getLotById(saleItemDTO.getLotId()));
        }
        return saleItem;
    }


}
