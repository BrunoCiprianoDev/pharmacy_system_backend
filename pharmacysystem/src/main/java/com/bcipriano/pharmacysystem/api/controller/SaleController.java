package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.saleDTO.SaleWriteDTO;
import com.bcipriano.pharmacysystem.api.dto.saleDTO.SaleReadDTO;
import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemWriteDTO;
import com.bcipriano.pharmacysystem.model.entity.Sale;
import com.bcipriano.pharmacysystem.model.entity.SaleItem;
import com.bcipriano.pharmacysystem.service.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/sales", produces = {"application/json"})
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    private final SaleItemService saleItemService;

    private final LotService lotService;

    private final EmployeeService employeeService;

    private final ClientService clientService;

    @GetMapping
    @ApiOperation(value = "Returns all sales registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of sales"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<SaleReadDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of sales to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Sale> salePage = saleService.getSale(pageable);

        List<SaleReadDTO> saleReadDTOList = new ArrayList<>();
        for (Sale sale : salePage) {
            List<SaleItem> saleItems = saleItemService.getSaleItemBySaleId(sale.getId());
            saleReadDTOList.add(SaleReadDTO.create(sale, saleItems));
        }

        return ResponseEntity.ok(saleReadDTOList);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Return sale by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of sales by ID."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No sales found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SaleReadDTO> get(
            @ApiParam(value = "Sale ID", required = true)
            @PathVariable("id") long id) {
        Sale sale = saleService.getSaleById(id);
        List<SaleItem> saleItems = saleItemService.getSaleItemBySaleId(id);
        SaleReadDTO saleReadDTO = SaleReadDTO.create(sale, saleItems);
        return ResponseEntity.ok(saleReadDTO);
    }

    @PostMapping
    @ApiOperation(value = "Creates a new Sale")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sale created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SaleReadDTO> post(
            @ApiParam(value = "The details of the sale to create", required = true)
            @RequestBody @Valid SaleWriteDTO saleWriteDTO) {
        Sale sale = SaleController.converter(saleWriteDTO, employeeService, clientService);

        List<SaleItem> saleItemList = new ArrayList<>();
        for (SaleItemWriteDTO saleItemWriteDTO : saleWriteDTO.getSaleItems()) {
            saleItemList.add(SaleItemController.converter(saleItemWriteDTO, lotService));
        }

        Sale saleSaved = saleService.saveSale(sale, saleItemList);
        List<SaleItem> saleItems = saleItemService.getSaleItemBySaleId(saleSaved.getId());
        SaleReadDTO saleReadDTO = SaleReadDTO.create(sale, saleItems);

        return new ResponseEntity(saleReadDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete an sale by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sale deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Sale ID", required = true)
            @PathVariable("id") Long id
    ) {
        saleService.deleteSale(id);
        return ResponseEntity.ok("Venda excluída com sucesso!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                }
        );
        return errors;
    }

    public static Sale converter(SaleWriteDTO saleWriteDTO, EmployeeService employeeService, ClientService clientService) {

        Sale sale = Sale.builder().saleDate(LocalDateTime.now()).build();

        if (saleWriteDTO.getEmployeeId() != null) {
            sale.setEmployee(employeeService.getEmployeeById(saleWriteDTO.getEmployeeId()).get());
        }

        if (saleWriteDTO.getClientId() != null) {
            sale.setClient(clientService.getClientById(saleWriteDTO.getClientId()).get());
        }

        return sale;
    }

}