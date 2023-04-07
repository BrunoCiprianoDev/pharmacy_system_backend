package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.LossDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import com.bcipriano.pharmacysystem.service.LossService;
import com.bcipriano.pharmacysystem.service.LotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/losses")
@RequiredArgsConstructor
public class LossController {

    private final LossService lossService;

    private final EmployeeService employeeService;

    private final LotService lotService;

    @GetMapping
    public ResponseEntity get() {
        List<Loss> lossList = lossService.getLoss();
        return ResponseEntity.ok(lossList.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity get(@PathVariable("query") String query) {
        List<Loss> lossList = lossService.getLossByQuery(query);
        return ResponseEntity.ok(lossList.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id){
        try{
            Loss lossResponse = lossService.getLossById(id);
            LossDTO lossDTO = LossDTO.create(lossResponse);
            return ResponseEntity.ok(lossDTO);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity post(@RequestBody LossDTO lossDTO) {
        try {
            Loss loss = converter(lossDTO);
            lossService.saveLoss(loss);
            return new ResponseEntity("Perda armazenada com sucesso!", HttpStatus.CREATED);
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody LossDTO lossDTO) {
        try {
            Loss loss = converter(lossDTO);
            loss.setId(id);
            lossService.saveLoss(loss);
            return ResponseEntity.ok("Registro de perda atualizado com sucesso.");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            lossService.deleteLoss(id);
            return ResponseEntity.ok("Registro de perdas excluída com sucesso!");
        } catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    public Loss converter(LossDTO lossDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Loss loss = modelMapper.map(lossDTO, Loss.class);

        loss.setRegisterDate(LocalDate.parse(lossDTO.getRegisterDate()));

        if (lossDTO.getLotId() != null) {
            loss.setLot(lotService.getLotById(lossDTO.getLotId()));
        }

        if (lossDTO.getEmployeeId() != null) {
            loss.setEmployee(employeeService.getEmployeeById(lossDTO.getEmployeeId()));
        }
        return loss;
    }

}
