package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.SupplierDTO;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Supplier;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/suppliers", produces = {"application/json"})
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    @ApiOperation("Returns all suppliers registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of suppliers"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<SupplierDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of suppliers to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Supplier> supplierPage = supplierService.getSupplier(pageable);
        return ResponseEntity.ok(supplierPage.stream().map(SupplierDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of supplier based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of suppliers that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<SupplierDTO>> get(
            @ApiParam(value = "The query to search for supplier")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of supplier to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Supplier> supplierPage = supplierService.getSupplierByQuery(query, pageable);
        return ResponseEntity.ok(supplierPage.stream().map(SupplierDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Returns suppliers by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of suppliers by ID."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No supplier found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SupplierDTO> get(@ApiParam(value = "Supplier ID") @PathVariable("id") Long id) {
        Optional<Supplier> supplierResponse = supplierService.getSupplierById(id);
        return ResponseEntity.ok(SupplierDTO.create(supplierResponse.get()));
    }

    @PostMapping
    @ApiOperation("Creates a new supplier")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Supplier created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SupplierDTO> post(
            @ApiParam(value = "The details of the supplier to create", required = true)
            @RequestBody @Valid SupplierDTO supplierDTO) {
        Supplier supplier = converter(supplierDTO);
        Supplier supplierResponse = supplierService.saveSupplier(supplier);
        return new ResponseEntity(SupplierDTO.create(supplierResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supplier updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<SupplierDTO> put(
            @ApiParam(value = "Supplier ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the supplier to update", required = true)
            @RequestBody @Valid SupplierDTO supplierDTO) {
        Supplier supplier = converter(supplierDTO);
        supplier.setId(id);
        Supplier supplierResponse = supplierService.updateSupplier(supplier);
        return new ResponseEntity(SupplierDTO.create(supplierResponse), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Supplier deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Employee ID", required = true)
            @PathVariable("id") Long id
    ) {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("Fornecedor excluído com sucesso!");
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

    public static Supplier converter(SupplierDTO supplierDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);

        Address address = modelMapper.map(supplierDTO, Address.class);
        supplier.setAddress(address);
        return supplier;
    }

}

