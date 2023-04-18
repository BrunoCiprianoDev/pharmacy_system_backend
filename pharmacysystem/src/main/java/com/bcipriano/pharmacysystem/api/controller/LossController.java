package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.LossDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import com.bcipriano.pharmacysystem.service.LossService;
import com.bcipriano.pharmacysystem.service.LotService;
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
@RequestMapping("/api/v1/losses")
@RequiredArgsConstructor
public class LossController {

    private final LossService lossService;

    private final EmployeeService employeeService;

    private final LotService lotService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Loss> lossPage = lossService.getLoss(pageable);
        return ResponseEntity.ok(lossPage.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity get(@RequestParam("query") String query,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Loss> lossPage = lossService.getLossByQueryLotNumber(query, pageable);
        return ResponseEntity.ok(lossPage.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/lotId/{id}")
    public ResponseEntity getLossByLotId(@PathVariable("id") Long id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Loss> lossPage = lossService.getLossByLotid(id, pageable);
            return ResponseEntity.ok(lossPage.stream().map(LossDTO::create).collect(Collectors.toList()));

        } catch (NotFoundException notFoundException) {
            return ResponseEntity.badRequest().body(notFoundException.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        try {
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
            Loss loss = converter(lossDTO, employeeService, lotService);
            lossService.saveLoss(loss);
            return new ResponseEntity("Registro de perda armazenada com sucesso!", HttpStatus.CREATED);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody LossDTO lossDTO) {
        try {
            Loss loss = converter(lossDTO, employeeService, lotService);
            loss.setId(id);
            lossService.updateLoss(loss);
            return ResponseEntity.ok("Registro de perda atualizado com sucesso.");
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
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

    public static Loss converter(LossDTO lossDTO, EmployeeService employeeService, LotService lotService) {
        ModelMapper modelMapper = new ModelMapper();
        Loss loss = modelMapper.map(lossDTO, Loss.class);

        loss.setRegisterDate(LocalDate.parse(lossDTO.getRegisterDate()));

        if (lossDTO.getLotId() != null) {
            Optional<Lot> lot = lotService.getLotById(lossDTO.getLotId());
            loss.setLot(lot.get());
        }

        if (lossDTO.getEmployeeId() != null) {
            Optional<Employee> employee = employeeService.getEmployeeById(lossDTO.getEmployeeId());
            loss.setEmployee(employee.get());
        }
        return loss;
    }

}