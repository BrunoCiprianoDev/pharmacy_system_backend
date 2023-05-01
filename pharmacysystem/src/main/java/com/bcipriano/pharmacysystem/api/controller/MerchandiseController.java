package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.MerchandiseDTO;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
import com.bcipriano.pharmacysystem.service.MerchandiseService;

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
@RequestMapping(value = "/api/v1/merchandises", produces = {"application/json"})
@RequiredArgsConstructor
public class MerchandiseController {

    private final MerchandiseService merchandiseService;

    private final DiscountGroupService discountGroupService;

    @GetMapping
    @ApiOperation("Returns all merchandises registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of merchandises"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<MerchandiseDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of merchandises to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Merchandise> merchandisePage = merchandiseService.getMerchandise(pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of merchandises based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of merchandises that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<MerchandiseDTO>> get(
            @ApiParam(value = "The query to search for merchandises")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of merchandises to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Merchandise> merchandisePage = merchandiseService.findMerchandiseByQuery(query, pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/discountGroupId/{id}")
    @ApiOperation(value = "Performs a paginated search of merchandises based on the provided discount group ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of merchandises that match the provided discount group ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<MerchandiseDTO>> getByDiscountGroupId(
            @ApiParam(value = "Discount group ID")
            @PathVariable("id") Long id,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of merchandises to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Merchandise> merchandisePage = merchandiseService.findMerchandiseByDiscountGroupId(id, pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/discountGroupName")
    @ApiOperation(value = "Performs a paginated search of merchandises based on the provided discount group name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of merchandises that match the provided discount group name"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<MerchandiseDTO>> getByDiscountGroupName(
            @ApiParam(value = "Discount group name")
            @RequestParam("name") String name,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of merchandises to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Merchandise> merchandisePage = merchandiseService.findMerchandiseByDiscountGroupName(name, pageable);
        return ResponseEntity.ok(merchandisePage.stream().map(MerchandiseDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/merchandiseCode")
    @ApiOperation(value = "Return merchandise by code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of merchandise by merchandise code"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No merchandise found with the provided code."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<MerchandiseDTO> getByMerchandiseCode(
            @ApiParam(value = "Merchandise code")
            @RequestParam("code") String code
    ) {
        Optional<Merchandise> merchandiseResponse = merchandiseService.findMerchandiseByCode(code);
        return ResponseEntity.ok(MerchandiseDTO.create(merchandiseResponse.get()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Return merchandise by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of merchandise by ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No employee found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<MerchandiseDTO> get(
            @ApiParam(value = "Merchandise ID")
            @PathVariable("id") Long id) {
        Optional<Merchandise> merchandiseResponse = merchandiseService.getMerchandiseById(id);
        return ResponseEntity.ok(MerchandiseDTO.create(merchandiseResponse.get()));
    }

    @PostMapping
    @ApiOperation("Creates a new merchandise")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Merchandise created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<MerchandiseDTO> post(
            @ApiParam(value = "The details of the merchandise to create", required = true)
            @RequestBody @Valid MerchandiseDTO merchandiseDTO) {
        Merchandise merchandise = converter(merchandiseDTO);
        Merchandise merchandiseResponse = merchandiseService.saveMerchandise(merchandise);
        return new ResponseEntity(MerchandiseDTO.create(merchandiseResponse), HttpStatus.CREATED);

    }

    @PutMapping("{id}")
    @ApiOperation("Update an merchandise")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchandise updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<MerchandiseDTO> update(
            @ApiParam(value = "Merchandise ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the merchandise to update", required = true)
            @RequestBody @Valid MerchandiseDTO merchandiseDTO
    ) {
        Merchandise merchandise = converter(merchandiseDTO);
        merchandise.setId(id);
        Merchandise merchandiseResponse = merchandiseService.updateMerchandise(merchandise);
        return new ResponseEntity(MerchandiseDTO.create(merchandiseResponse), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an merchandise by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Merchandise deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Merchandise ID", required = true)
            @PathVariable("id") Long id) {
            merchandiseService.deleteMerchandise(id);
            return ResponseEntity.ok("Mercadoria excluída com sucesso!");
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

    public Merchandise converter(MerchandiseDTO merchandiseDTO) {

        ModelMapper modelMapper = new ModelMapper();
        Merchandise merchandise = modelMapper.map(merchandiseDTO, Merchandise.class);

        if (merchandiseDTO.getDiscountGroupId() != null) {
            Optional<DiscountGroup> discountGroupServiceOptional = discountGroupService.getDiscountGroupById(merchandiseDTO.getDiscountGroupId());
            merchandise.setDiscountGroup(discountGroupServiceOptional.get());
        }

        return merchandise;
    }
}