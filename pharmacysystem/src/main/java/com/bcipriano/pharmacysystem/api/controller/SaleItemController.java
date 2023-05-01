package com.bcipriano.pharmacysystem.api.controller;


import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemReadDTO;
import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemWriteDTO;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.service.LotService;
import com.bcipriano.pharmacysystem.service.SaleItemService;

import com.bcipriano.pharmacysystem.service.SaleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/saleItems", produces = {"application/json"})
@RequiredArgsConstructor
public class SaleItemController {

    private final SaleService saleService;

    private final SaleItemService saleItemService;

    private final LotService lotService;

    @GetMapping("{id}")
    @ApiOperation("Return sale item by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a list of sale item"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SaleItemReadDTO> get(
            @ApiParam(value = "Sale item ID")
            @PathVariable("id") Long id
    ) {
        Optional<SaleItem> saleItem = saleItemService.getSaleItemById(id);
        return new ResponseEntity(SaleItemReadDTO.create(saleItem.get()), HttpStatus.OK);
    }

    @GetMapping("/sale/{id}")
    @ApiOperation(value = "Performs a list search of sale item based on the provided sale ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a list of sale item that match the provided sale ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<SaleItemReadDTO>> getSaleItemsBySaleId(
            @ApiParam(value = "The details of the sale to create", required = true)
            @PathVariable("saleId") Long saleId) {
        List<SaleItem> saleItemList = saleItemService.getSaleItemBySaleId(saleId);
        return ResponseEntity.ok(saleItemList.stream().map(SaleItemReadDTO::create).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an sale item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sale item updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SaleItemReadDTO> put(
            @ApiParam(value = "Sale item ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the purchase to update", required = true)
            @RequestBody @Valid SaleItemWriteDTO saleItemWriteDTO
    ) {
        SaleItem saleItem = SaleItemController.converter(saleItemWriteDTO, lotService);
        saleItem.setId(id);
        SaleItem saleItemUpdated = saleItemService.updateSaleItem(saleItem);
        return new ResponseEntity(SaleItemReadDTO.create(saleItemUpdated), HttpStatus.OK);
    }

    public static SaleItem converter(SaleItemWriteDTO saleItemWriteDTO, LotService lotService) {

        SaleItem saleItem = SaleItem.builder().build();

        saleItem.setUnits(saleItemWriteDTO.getUnits());

        if (saleItemWriteDTO.getLotId() != null) {
            saleItem.setLot(lotService.getLotById(saleItemWriteDTO.getLotId()).get());
        }
        return saleItem;
    }
}