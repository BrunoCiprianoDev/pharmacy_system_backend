package com.bcipriano.pharmacysystem.api.controller;


import com.bcipriano.pharmacysystem.api.dto.SaleDTO;
import com.bcipriano.pharmacysystem.api.dto.SaleItemDTO;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    private final SaleItemService saleItemService;

    private final LotService lotService;

    private final EmployeeService employeeService;

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Sale> salePage = saleService.getSale(pageable);

        List<SaleDTO> saleDTOList = new ArrayList<>();
        for(Sale sale : salePage) {
            List<SaleItem> saleItems = saleItemService.getSaleItemBySaleId(sale.getId());
            saleDTOList.add(SaleDTO.create(sale, saleItems));
        }

        return ResponseEntity.ok(saleDTOList);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody SaleDTO saleDTO){
        try {
            Sale sale = SaleController.converter(saleDTO, employeeService, clientService);

            List<SaleItem> saleItemList = new ArrayList<>();
            for(SaleItemDTO saleItemDTO : saleDTO.getSaleItemsDTO()){
                SaleItem saleItem = SaleItemController.converter(saleItemDTO, saleService, lotService);
                saleItemList.add(saleItem);
            }

            saleService.saveSale(sale, saleItemList);

            return ResponseEntity.ok("Venda concluída com sucesso!");
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
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

    public static Sale converter(SaleDTO saleDTO, EmployeeService employeeService, ClientService clientService) {

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
