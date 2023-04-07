package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.ReturnDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.service.ReturnService;
import com.bcipriano.pharmacysystem.service.SaleItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/returns")
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnService returnService;

    private final SaleItemService saleItemService;


    @GetMapping
    public ResponseEntity get() {
        List<Return> returnList = returnService.getReturn();
        return ResponseEntity.ok(returnList.stream().map(ReturnDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
            Return returnResponse = returnService.getReturnById(id);
            ReturnDTO returnDTO = ReturnDTO.create(returnResponse);
            return ResponseEntity.ok(returnDTO);
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @GetMapping("/saleItem/{id}")
    public ResponseEntity getReturnBySaleItemId(@PathVariable("id") Long saleItemId){
        try {
            Return returnObj = returnService.getReturnBySaleItemId(saleItemId);
            ReturnDTO returnDTO = ReturnDTO.create(returnObj);
            return ResponseEntity.ok(returnDTO);
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody ReturnDTO returnDTO){
        try {
            Return returnObj = converter(returnDTO);
            returnService.saveReturn(returnObj);
            return new ResponseEntity("Registro de devolução armazenado com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody ReturnDTO returnDTO) {
        try {
            Return returnObj = converter(returnDTO);
            returnObj.setId(id);
            returnService.saveReturn(returnObj);
            return ResponseEntity.ok("Registro de devolução atualizado com sucesso!");
        } catch(BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        try {
            returnService.deleteReturn(id);
            return ResponseEntity.ok("Registro de devolução com id inválido.");
        } catch(BusinessRuleException businessRuleException){
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Return converter(ReturnDTO returnDTO){
        ModelMapper modelMapper = new ModelMapper();
        Return returnObj = modelMapper.map(returnDTO, Return.class);

        returnObj.setRegisterDate(LocalDate.parse(returnDTO.getRegisterDate()));

        if(returnDTO.getSaleItemId() != null){
            returnObj.setSaleItem(saleItemService.getSaleItemById(returnDTO.getSaleItemId()));
        }

        return returnObj;
    }

}
