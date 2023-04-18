package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.DiscountGroupDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
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
import java.util.Optional;
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
        Pageable pageable = PageRequest.of(page, size);
        Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroupByQuery(query, pageable);
        return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/startDate")
    public ResponseEntity getByStartDate(@RequestParam("startDate") String startDate,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroupByStartDate(startDate, pageable);
            return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
        }

    }

    @GetMapping("/finalDate")
    public ResponseEntity getByFinalDate(@RequestParam("finalDate") String finalDate,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroupByFinalDate(finalDate, pageable);
            return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Optional<DiscountGroup> discountGroupResponse = discountGroupService.getDiscountGroupById(id);
            return ResponseEntity.ok(discountGroupResponse.map(DiscountGroupDTO::create));
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody DiscountGroupDTO discountGroupDTO) {
        try {
            DiscountGroup discountGroup = converter(discountGroupDTO);
            discountGroupService.saveDiscountGroup(discountGroup);
            return new ResponseEntity("Grupo de desconto armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return new ResponseEntity(businessRuleException.getMessage(), businessRuleException.getStatus());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity put(@PathVariable("id") Long id, @RequestBody DiscountGroupDTO discountGroupDTO) {
        try {
            DiscountGroup discountGroup = converter(discountGroupDTO);
            discountGroup.setId(id);
            discountGroupService.updateDiscountGroup(discountGroup);
            return ResponseEntity.ok("Grupo de desconto atualizado com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return new ResponseEntity(businessRuleException.getMessage(), businessRuleException.getStatus());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        try {
            discountGroupService.deleteDiscountGroup(id);
            return ResponseEntity.ok("Grupo de desconto excluído com sucesso!");
        } catch(NotFoundException notFoundException) {
            return new ResponseEntity(notFoundException.getMessage(), notFoundException.getStatus());
        }
    }

    public static DiscountGroup converter(DiscountGroupDTO discountGroupDTO) {

        ModelMapper modelMapper = new ModelMapper();
        DiscountGroup discountGroup = modelMapper.map(discountGroupDTO, DiscountGroup.class);

        discountGroup.setStartDate(LocalDate.parse(discountGroupDTO.getStartDate()));
        discountGroup.setFinalDate(LocalDate.parse(discountGroupDTO.getFinalDate()));

        return discountGroup;

    }

}
