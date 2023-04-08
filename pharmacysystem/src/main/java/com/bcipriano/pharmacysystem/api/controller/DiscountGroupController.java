package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.DiscountGroupDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
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
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class DiscountGroupController {

    private final DiscountGroupService discountGroupService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroup(pageable);
        return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        int startItem = size * page;
        List<DiscountGroup> pageList;
        List<DiscountGroup> discounts = discountGroupService.getDiscountGroupByQuery(query);

        if(discounts.size() < startItem){
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + size, discounts.size());
            pageList = discounts.subList(startItem, toIndex);
        }

        return ResponseEntity.ok(pageList.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            DiscountGroup discountGroupResponse = discountGroupService.getDiscountGroupById(id);
            DiscountGroupDTO discountGroupDTO = DiscountGroupDTO.create(discountGroupResponse);
            return ResponseEntity.ok(discountGroupDTO);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody DiscountGroupDTO discountGroupDTO) {
        try {
            DiscountGroup discountGroup = converter(discountGroupDTO);
            discountGroupService.saveDiscountGroup(discountGroup);
            return new ResponseEntity("Dados armazenados com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody DiscountGroupDTO discountGroupDTO) {
        try {
            DiscountGroup discountGroup = converter(discountGroupDTO);
            discountGroup.setId(id);
            discountGroupService.updateDiscountGroup(discountGroup);
            return ResponseEntity.ok("Mercadoria atualizada com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            discountGroupService.deleteDiscountGroup(id);
            return ResponseEntity.ok("Grupo de desconto excluído com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public DiscountGroup converter(DiscountGroupDTO discountGroupDTO) {
        ModelMapper modelMapper = new ModelMapper();
        DiscountGroup discountGroup = modelMapper.map(discountGroupDTO, DiscountGroup.class);

        discountGroup.setStartDate(LocalDate.parse(discountGroupDTO.getStartDate()));
        discountGroup.setFinalDate(LocalDate.parse(discountGroupDTO.getFinalDate()));

        return discountGroup;
    }

}
