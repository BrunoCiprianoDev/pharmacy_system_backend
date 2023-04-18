package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.MerchandiseDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.enums.Department;
import com.bcipriano.pharmacysystem.model.entity.enums.StorageTemperature;
import com.bcipriano.pharmacysystem.model.entity.enums.Stripe;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
import com.bcipriano.pharmacysystem.service.LotService;
import com.bcipriano.pharmacysystem.service.MerchandiseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/merchandises")
@RequiredArgsConstructor
public class MerchandiseController {

    private final MerchandiseService merchandiseService;

    private final DiscountGroupService discountGroupService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Merchandise> merchandisePage = merchandiseService.getMerchandise(pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Merchandise> merchandisePage = merchandiseService.findMerchandiseByQuery(query, pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/discountGroupId/{id}")
    public ResponseEntity getByDiscountGroupId(@PathVariable("id") Long id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Merchandise> merchandisePage = merchandiseService.findMerchandiseByDiscountGroupId(id, pageable);
            return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.badRequest().body(notFoundException.getMessage());
        }
    }

    @GetMapping("/discountGroupName")
    public ResponseEntity getByDiscountGroupName(@RequestParam("name") String name,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Merchandise> merchandisePage = merchandiseService.findMerchandiseByDiscountGroupName(name, pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/merchandiseCode")
    public ResponseEntity getByMerchandiseCode(@RequestParam("code") String code){
        Optional<Merchandise> merchandise = merchandiseService.findMerchandiseByCode(code);
        return ResponseEntity.ok(merchandise.map(MerchandiseDTO::create));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Optional<Merchandise> merchandise = merchandiseService.getMerchandiseById(id);
            return ResponseEntity.ok(merchandise.map(MerchandiseDTO::create));
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody MerchandiseDTO merchandiseDTO) {
        try {
            Merchandise merchandise = converter(merchandiseDTO);
            merchandiseService.saveMerchandise(merchandise);
            return new ResponseEntity("Mercadoria armazenada com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody MerchandiseDTO merchandiseDTO) {
        try {
            Merchandise merchandise = converter(merchandiseDTO);
            merchandise.setId(id);
            merchandiseService.updateMerchandise(merchandise);
            return ResponseEntity.ok("Mercadoria atualizada com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            merchandiseService.deleteMerchandise(id);
            return ResponseEntity.ok("Mercadoria excluída com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Merchandise converter(MerchandiseDTO merchandiseDTO) {

        ModelMapper modelMapper = new ModelMapper();
        Merchandise merchandise = modelMapper.map(merchandiseDTO, Merchandise.class);

        if (merchandiseDTO.getDiscountGroupId() != null) {
            Optional<DiscountGroup> discountGroupServiceOptional = discountGroupService.getDiscountGroupById(merchandiseDTO.getDiscountGroupId());
            merchandise.setDiscountGroup(discountGroupServiceOptional.get());
        }

        merchandise.setDepartment(Department.fromString(merchandiseDTO.getDepartment()));
        merchandise.setStripe(Stripe.fromString(merchandiseDTO.getStripe()));
        merchandise.setStorageTemperature(StorageTemperature.fromString(merchandiseDTO.getStorageTemperature()));

        return merchandise;
    }
}