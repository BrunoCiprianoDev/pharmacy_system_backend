package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.PurchaseDTO;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
import com.bcipriano.pharmacysystem.service.PurchaseService;
import com.bcipriano.pharmacysystem.service.SupplierService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/purchases", produces = {"application/json"})
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    private final SupplierService supplierService;

    @GetMapping
    @ApiOperation("Returns all purchases registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of purchase"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<PurchaseDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of purchases to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Purchase> purchasePage = purchaseService.getPurchase(pageable);
        return ResponseEntity.ok(purchasePage.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of purchases based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of purchases that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<PurchaseDTO>> get(
            @ApiParam(value = "The query to search for purchases", required = true)
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of purchases to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Purchase> purchasePage = purchaseService.findPurchaseByQuery(query, pageable);
        return ResponseEntity.ok(purchasePage.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/supplier/{id}")
    @ApiOperation(value = "Performs a paginated search of purchases based on the provided supplier ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of purchases that match the provided supplier ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<PurchaseDTO>> getByPurchaseBySupplierId(
            @ApiParam(value = "Supplier ID", required = true)
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of purchases to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Purchase> purchasePage = purchaseService.getPurchaseBySupplierId(id, pageable);
        return ResponseEntity.ok(purchasePage.stream().map(PurchaseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/noteNumber")
    @ApiOperation(value = "Return purchase by note number(QUERY).")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of purchase by note number"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No number found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<PurchaseDTO> getPurchaseByNoteNumber(
            @ApiParam(value = "Note number", required = true)
            @RequestParam("noteNumber") String noteNumber) {
        Optional<Purchase> purchaseResponse = purchaseService.getPurchaseNoteNumber(noteNumber);
        return ResponseEntity.ok(PurchaseDTO.create(purchaseResponse.get()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Return purchase by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of purchase by ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No purchase found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<PurchaseDTO> get(
            @ApiParam(value = "Purchase ID", required = true)
            @PathVariable("id") Long id
    ) {
        Optional<Purchase> purchaseResponse = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(PurchaseDTO.create(purchaseResponse.get()));
    }

    @PostMapping
    @ApiOperation("Creates a new purchase")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Purchase created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<PurchaseDTO> post(
            @ApiParam(value = "The details of the purchase to create", required = true)
            @RequestBody @Valid PurchaseDTO purchaseDTO
    ) {
        Purchase purchase = converter(purchaseDTO);
        Purchase purchaseResponse = purchaseService.savePurchase(purchase);
        return new ResponseEntity(PurchaseDTO.create(purchaseResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an purchase")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Purchase updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<PurchaseDTO> update(
            @ApiParam(value = "Purchase ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the purchase to update", required = true)
            @RequestBody @Valid PurchaseDTO purchaseDTO) {
        Purchase purchase = converter(purchaseDTO);
        purchase.setId(id);
        Purchase purchaseResponse = purchaseService.updatePurchase(purchase);
        return new ResponseEntity(PurchaseDTO.create(purchaseResponse), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an purchase by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Purchase deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Merchandise ID", required = true)
            @PathVariable("id") Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok("Compra excluída com sucesso!");
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

    public Purchase converter(PurchaseDTO purchaseDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Purchase purchase = modelMapper.map(purchaseDTO, Purchase.class);

        purchase.setPurchaseDate(LocalDate.parse(purchaseDTO.getPurchaseDate()));

        if (purchaseDTO.getSupplierId() != null) {
            Optional<Supplier> supplier = supplierService.getSupplierById(purchaseDTO.getSupplierId());
            purchase.setSupplier(supplier.get());
        }
        return purchase;
    }
}