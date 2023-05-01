package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.LotDTO;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.model.entity.Merchandise;
import com.bcipriano.pharmacysystem.model.entity.Purchase;
import com.bcipriano.pharmacysystem.service.LotService;
import com.bcipriano.pharmacysystem.service.MerchandiseService;
import com.bcipriano.pharmacysystem.service.PurchaseService;

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
@RequestMapping(value = "/api/v1/lots", produces = {"application/json"})
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    private final MerchandiseService merchandiseService;

    private final PurchaseService purchaseService;

    @GetMapping
    @ApiOperation("Returns all lots registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of lots"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LotDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of lots to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Lot> lotPage = lotService.getLot(pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of lots based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of lots that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LotDTO>> get(
            @ApiParam(value = "The query to search for lots")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of lots to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Lot> lotPage = lotService.getLotsByPurchaseNoteNumber(query, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/purchase/{id}")
    @ApiOperation(value = "Performs a paginated search of lots based on the provided supplier ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of lots that match the provided purchase ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LotDTO>> getByPurchaseId(
            @ApiParam(value = "Purchase ID", required = true)
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of lots to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Lot> lotPage = lotService.getLotsByPurchaseId(id, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/merchandise/{id}")
    @ApiOperation(value = "Performs a paginated search of lots based on the provided merchandise ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of lots that match the provided merchandise ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LotDTO>> getByMerchandiseId(
            @ApiParam(value = "Merchandise ID", required = true)
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of lots to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Lot> lotPage = lotService.getLotsByMerchandiseId(id, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/getUnitsByMerchandiseId/{id}")
    @ApiOperation(value = "Performs a units search of lots based on the provided merchandise ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a units of lots that match the provided merchandise ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<Integer> getUnitsByMerchandiseId(
            @ApiParam(value = "Merchandise ID", required = true)
            @PathVariable("id") long merchandiseId
    ) {
        Integer units = lotService.getCurrentStockByMerchandiseId(merchandiseId);
        return ResponseEntity.ok(units);
    }

    @GetMapping("/expiringLotsByDays/{days}")
    @ApiOperation(value = "Performs a search for merchandise lots that have the expiration date for a value in days.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of lots"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No number found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LotDTO>> getLotsByExpiring(
            @ApiParam(value = "Number of days", required = true)
            @PathVariable("days") Integer days,
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of lots to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Lot> lotPage = lotService.getLotExpiringWithinDays(days, pageable);
        return ResponseEntity.ok(lotPage.stream().map(LotDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Return lots by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of lots by ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No lot found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<LotDTO> getLotById(
            @ApiParam(value = "Lot ID", required = true)
            @PathVariable("id") long id
    ) {
        Optional<Lot> lot = lotService.getLotById(id);
        return ResponseEntity.ok(LotDTO.create(lot.get()));
    }

    @PostMapping
    @ApiOperation("Creates a new lot")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lot created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<LotDTO> post(
            @ApiParam(value = "The details of the lot to create", required = true)
            @RequestBody @Valid LotDTO lotDTO
    ) {
        Lot lot = converter(lotDTO);
        Lot lotResponse = lotService.saveLot(lot);
        return new ResponseEntity(LotDTO.create(lotResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an lot")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lot updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<LotDTO> update(
            @ApiParam(value = "Lot ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the lot to update", required = true)
            @RequestBody @Valid LotDTO lotDTO) {
        Lot lot = converter(lotDTO);
        lot.setId(id);
        Lot lotResponse = lotService.updateLot(lot);
        return new ResponseEntity(lotResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an Lot by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lot deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Lot ID", required = true)
            @PathVariable("id") Long id) {
        lotService.deleteLot(id);
        return ResponseEntity.ok("Lote excluído com sucesso!");
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

    public Lot converter(LotDTO lotDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Lot lot = modelMapper.map(lotDTO, Lot.class);

        lot.setExpirationDate(LocalDate.parse(lotDTO.getExpirationDate()));

        if (lotDTO.getPurchaseId() != null) {
            Optional<Merchandise> merchandise = merchandiseService.getMerchandiseById(lotDTO.getMerchandiseId());
            lot.setMerchandise(merchandise.get());
        }
        if (lotDTO.getPurchaseId() != null) {
            Optional<Purchase> purchase = purchaseService.getPurchaseById(lotDTO.getPurchaseId());
            lot.setPurchase(purchase.get());
        }
        return lot;
    }
}