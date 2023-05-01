package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.ReturnDTO;
import com.bcipriano.pharmacysystem.model.entity.Return;
import com.bcipriano.pharmacysystem.service.ReturnService;
import com.bcipriano.pharmacysystem.service.SaleItemService;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/returns", produces = {"application/json"})
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnService returnService;

    private final SaleItemService saleItemService;

    @GetMapping
    @ApiOperation(value = "Returns all returned items registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of returns"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<ReturnDTO>> get(
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
        Page<Return> returnPage = returnService.getReturn(pageable);
        return ResponseEntity.ok(returnPage.stream().map(ReturnDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Return by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list by ID."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No sales found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ReturnDTO> get(
            @ApiParam(value = "Return ID")
            @PathVariable("id") Long id) {
        Return returnResponse = returnService.getReturnById(id);
        ReturnDTO returnDTO = ReturnDTO.create(returnResponse);
        return ResponseEntity.ok(returnDTO);
    }

    @GetMapping("/saleItem/{id}")
    @ApiOperation(value = "Performs a paginated search of retuns by saleItem ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of returns that match the provided sale item ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ReturnDTO> getReturnBySaleItemId(
            @ApiParam(value = "Return ID", required = true)
            @PathVariable("id") Long saleItemId) {
        Return returnObj = returnService.getReturnBySaleItemId(saleItemId);
        ReturnDTO returnDTO = ReturnDTO.create(returnObj);
        return ResponseEntity.ok(returnDTO);
    }

    @PostMapping
    @ApiOperation("Creates a new Returns")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Returns created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ReturnDTO> post(
            @ApiParam(value = "The details of the return to create", required = true)
            @RequestBody @Valid ReturnDTO returnDTO
    ) {
        Return returnObj = converter(returnDTO);
        Return returnResponse = returnService.saveReturn(returnObj);
        return new ResponseEntity(ReturnDTO.create(returnResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an return")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Purchase return successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<ReturnDTO> update(
            @ApiParam(value = "Return ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the return to update", required = true)
            @RequestBody ReturnDTO returnDTO
    ) {
        Return returnObj = converter(returnDTO);
        returnObj.setId(id);
        Return returnResponse = returnService.saveReturn(returnObj);
        return new ResponseEntity(ReturnDTO.create(returnResponse), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an return by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Return ID", required = true)
            @PathVariable("id") Long id
    ) {
        returnService.deleteReturn(id);
        return ResponseEntity.ok("Registro de devolução com id inválido.");
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

    public Return converter(ReturnDTO returnDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Return returnObj = modelMapper.map(returnDTO, Return.class);

        returnObj.setRegisterDate(LocalDate.parse(returnDTO.getRegisterDate()));

        if (returnDTO.getSaleItemId() != null) {
            returnObj.setSaleItem(saleItemService.getSaleItemById(returnDTO.getSaleItemId()).get());
        }
        return returnObj;
    }

}