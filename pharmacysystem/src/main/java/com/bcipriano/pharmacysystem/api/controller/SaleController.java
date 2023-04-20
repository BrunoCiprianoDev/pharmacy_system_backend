package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.SaleDTO;
import com.bcipriano.pharmacysystem.api.dto.SaleItemDTO;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.service.*;
import lombok.RequiredArgsConstructor;
import org.h2.store.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

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
    public ResponseEntity get(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Sale> salePage = saleService.getSale(pageable);

        List<SaleDTO> saleDTOList = new ArrayList<>();
        for(Sale sale : salePage) {
            List<SaleItem> saleItems = saleItemService.getSaleItemBySaleId(sale.getId());
            saleDTOList.add(SaleDTO.create(sale, saleItems));
        }

        return ResponseEntity.ok(saleDTOList);
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") long id) {

        try {
            Sale sale = saleService.getSaleById(id);
            List<SaleItem> saleItems = saleItemService.getSaleItemBySaleId(id);
            SaleDTO saleDTO = SaleDTO.create(sale, saleItems);
            return ResponseEntity.ok(saleDTO);
        } catch(RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

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

    @PutMapping("{id}")
    public ResponseEntity put(@RequestBody SaleDTO saleDTO, @PathVariable("id") Long id) {
        try {
            Sale sale = SaleController.converter(saleDTO, employeeService, clientService);
            sale.setId(id);
            saleService.updateSale(sale);
            return ResponseEntity.ok("Venda atualizada com sucesso!");
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().body(runtimeException.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") Long id){
        try {

            saleService.deleteSale(id);
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
            sale.setEmployee(employeeService.getEmployeeById(saleDTO.getEmployeeId()).get());
        }

        if(saleDTO.getClientId() != null){
            sale.setClient(clientService.getClientById(saleDTO.getClientId()).get());
        }

        return sale;
    }

}