package com.bcipriano.pharmacysystem.api.controller;


import com.bcipriano.pharmacysystem.api.dto.SaleDTO;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.service.ClientService;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import com.bcipriano.pharmacysystem.service.SaleItemService;
import com.bcipriano.pharmacysystem.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {


    private final SaleService saleService;

    private final SaleItemService saleItemService;

    private final EmployeeService employeeService;

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Sale> salePage = saleService.getSale(pageable);
        return ResponseEntity.ok(salePage.stream().map(SaleDTO::create).collect(Collectors.toList()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        try {
            saleService.getSaleById(id);
            return ResponseEntity.ok("Venda excluída com sucesso!");
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
    }

    public Sale converter(SaleDTO saleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Sale sale = modelMapper.map(saleDTO, Sale.class);

        sale.setSaleDate(LocalDate.parse(saleDTO.getSaleDate()));

        if(saleDTO.getEmployeeId() != null){
            sale.setEmployee(employeeService.getEmployeeById(saleDTO.getEmployeeId()));
        }

        if(saleDTO.getClientId() != null){
            sale.setClient(clientService.getClientById(saleDTO.getClientId()));
        }
        return sale;
    }

}
