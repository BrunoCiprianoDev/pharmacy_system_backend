package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.MerchandiseDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
import com.bcipriano.pharmacysystem.service.MerchandiseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/merchandises")
@RequiredArgsConstructor
public class MerchandiseController {

    private final MerchandiseService merchandiseService;

    private final DiscountGroupService discountGroupService;

    @GetMapping()
    public ResponseEntity get() {
        List<Merchandise> merchandises = merchandiseService.getMerchandise();
        return ResponseEntity.ok(merchandises.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity get(@PathVariable("query") String query) {
        List<Merchandise> merchandises = merchandiseService.findMerchandiseByQuery(query);
        return ResponseEntity.ok(merchandises.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Merchandise merchandiseResponse = merchandiseService.getMerchandiseById(id);
            MerchandiseDTO merchandiseDTO = MerchandiseDTO.create(merchandiseResponse);
            return ResponseEntity.ok(merchandiseDTO);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping()
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
        if (merchandiseDTO.getIdDiscountGroup() != null) {
            merchandise.setDiscountGroup(discountGroupService.getDiscountGroupById(merchandiseDTO.getIdDiscountGroup()));
        }
        return merchandise;
    }
}
