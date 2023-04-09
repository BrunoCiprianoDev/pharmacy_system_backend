package com.bcipriano.pharmacysystem.api.controller;


import com.bcipriano.pharmacysystem.api.dto.LotDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.service.LotService;
import com.bcipriano.pharmacysystem.service.MerchandiseService;
import com.bcipriano.pharmacysystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    private final MerchandiseService merchandiseService;

    private final PurchaseService purchaseService;

    @GetMapping()
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lot> lotPage = lotService.getLot(pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        int startItem = size * page;
        List<Lot> pageList;
        List<Lot> lotList = lotService.getLotsByPurchaseNoteNumber(query);

        if (lotList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int index = Math.min(startItem + size, lotList.size());
            pageList = lotList.subList(startItem, index);
        }

        return ResponseEntity.ok(pageList.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity getByPurchaseId(@PathVariable("id") long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {

        int startItem = size * page;
        List<Lot> pageList;
        List<Lot> lotList = lotService.getLotsByPurchaseId(id);

        if (lotList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int index = Math.min(startItem + size, lotList.size());
            pageList = lotList.subList(startItem, index);
        }

        return ResponseEntity.ok(pageList.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/merchandise/{id}")
    public ResponseEntity getByMerchandiseId(@PathVariable("id") long id,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {

        int startItem = size * page;
        List<Lot> pageList;
        List<Lot> lotList = lotService.getLotsByMerchandiseId(id);

        if (lotList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int index = Math.min(startItem + size, lotList.size());
            pageList = lotList.subList(startItem, index);
        }

        return ResponseEntity.ok(pageList.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody LotDTO lotDTO) {
        try {
            Lot lot = converter(lotDTO);
            lotService.saveLot(lot);
            return new ResponseEntity("Lote armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody LotDTO lotDTO) {
        try {
            Lot lot = converter(lotDTO);
            lot.setId(id);
            lotService.saveLot(lot);
            return ResponseEntity.ok("Lote atualizado com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            lotService.deleteLot(id);
            return ResponseEntity.ok("Lote excluído com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Lot converter(LotDTO lotDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Lot lot = modelMapper.map(lotDTO, Lot.class);

        lot.setExpirationDate(LocalDate.parse(lotDTO.getExpirationDate()));

        if (lotDTO.getPurchaseId() != null) {
            lot.setMerchandise(merchandiseService.getMerchandiseById(lotDTO.getMerchandiseId()));
        }
        if (lotDTO.getPurchaseId() != null) {
            lot.setPurchase(purchaseService.getPurchaseById(lotDTO.getPurchaseId()));
        }
        return lot;
    }
}


//INSERT INTO management.purchase (note_number, purchase_date, total, id_supplier) VALUES ('12345', '2023-04-01', 2599, 4);
