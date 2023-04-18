package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.LotDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
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
import java.util.Optional;
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
        Pageable pageable = PageRequest.of(page, size);
        Page<Lot> lotPage = lotService.getLotsByPurchaseNoteNumber(query, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity getByPurchaseId(@PathVariable("id") long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lot> lotPage = lotService.getLotsByPurchaseId(id, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/merchandise/{merchandiseId}")
    public ResponseEntity getByMerchandiseId(@PathVariable("merchandiseId") long merchandiseId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lot> lotPage = lotService.getLotsByMerchandiseId(merchandiseId, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/getUnitsByMerchandiseId/{merchandiseId}")
    public ResponseEntity getUnitsByMerchandiseId(@PathVariable("merchandiseId") long merchandiseId){
        try {
            Integer units = lotService.getCurrentStockByMerchandiseId(merchandiseId);
            return ResponseEntity.ok(units);
        } catch(NotFoundException notFoundException) {
            return ResponseEntity.badRequest().body(notFoundException.getMessage());
        }
    }
    @GetMapping("/expiringLotsByDays/{days}")
    public ResponseEntity getLotsByExpiring(@PathVariable("days") Integer days,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Lot> lotPage = lotService.getLotExpiringWithinDays(days, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity getLotById(@PathVariable("id") long id){
        try {
            Optional<Lot> lot = lotService.getLotById(id);
            return ResponseEntity.ok(lot.map(LotDTO::create));
        }catch(BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
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
            Optional<Merchandise> merchandise = merchandiseService.getMerchandiseById(lotDTO.getMerchandiseId());
            lot.setMerchandise(merchandise.get());
        }
        if (lotDTO.getPurchaseId() != null) {
            Optional<Purchase> purchase = purchaseService.getPurchaseById(lotDTO.getPurchaseId());
            lot.setPurchase(purchase.get());
        }
        return lot;
    }
}