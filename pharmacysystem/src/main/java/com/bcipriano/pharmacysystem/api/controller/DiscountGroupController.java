package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.DiscountGroupDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.InvalidIdException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class DiscountGroupController {

    private final DiscountGroupService discountGroupService;

    @GetMapping()
    public ResponseEntity get() {
        List<DiscountGroup> discountGroup = discountGroupService.getDiscountGroup();
        return ResponseEntity.ok(discountGroup.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity get(@PathVariable("query") String query) {
        List<DiscountGroup> discountGroup = discountGroupService.getDiscountGroupByQuery(query);
        return ResponseEntity.ok(discountGroup.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            DiscountGroup discountGroupResponse = discountGroupService.getDiscountGroupById(id);
            DiscountGroupDTO discountGroupDTO = DiscountGroupDTO.create(discountGroupResponse);
            return ResponseEntity.ok(discountGroupDTO);
        } catch (InvalidIdException invalidIdException) {
            return ResponseEntity.badRequest().body(invalidIdException.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody DiscountGroupDTO discountGroupDTO) {
        try {
            DiscountGroup discountGroup = converter(discountGroupDTO);
            DiscountGroup discountGroupResponse = discountGroupService.saveDiscountGroup(discountGroup);
            return ResponseEntity.ok(discountGroupResponse);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody DiscountGroupDTO discountGroupDTO) {
        try {
            DiscountGroup discountGroup = converter(discountGroupDTO);
            discountGroup.setId(id);
            DiscountGroup discountGroupResponse = discountGroupService.updateDiscountGroup(discountGroup);
            return ResponseEntity.ok(discountGroupResponse);
        } catch (InvalidIdException invalidIdException) {
            return ResponseEntity.badRequest().body(invalidIdException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            discountGroupService.deleteDiscountGroup(id);
            return ResponseEntity.ok("Grupo de desconto excluído com sucesso!");
        } catch (InvalidIdException invalidIdException) {
            return ResponseEntity.badRequest().body(invalidIdException.getMessage());
        }
    }

    public DiscountGroup converter(DiscountGroupDTO discountGroupDTO) {
        ModelMapper modelMapper = new ModelMapper();
        DiscountGroup discountGroup = modelMapper.map(discountGroupDTO, DiscountGroup.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        discountGroup.setStartDate(LocalDate.parse(discountGroupDTO.getStartDate(), formatter));
        discountGroup.setFinalDate(LocalDate.parse(discountGroupDTO.getFinalDate(), formatter));

        return discountGroup;
    }

}
