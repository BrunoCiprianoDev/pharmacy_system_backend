package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.LossDTO;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.model.entity.Loss;
import com.bcipriano.pharmacysystem.model.entity.Lot;
import com.bcipriano.pharmacysystem.service.EmployeeService;
import com.bcipriano.pharmacysystem.service.LossService;
import com.bcipriano.pharmacysystem.service.LotService;

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
@RequestMapping(value = "/api/v1/losses", produces = {"application/json"})
@RequiredArgsConstructor
public class LossController {

    private final LossService lossService;

    private final EmployeeService employeeService;

    private final LotService lotService;

    @GetMapping
    @ApiOperation("Returns all loss registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of loss"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LossDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of loss to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Loss> lossPage = lossService.getLoss(pageable);
        return ResponseEntity.ok(lossPage.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of loss based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of loss that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LossDTO>> get(
            @ApiParam(value = "The query to search for loss")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of loss to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Loss> lossPage = lossService.getLossByQueryLotNumber(query, pageable);
        return ResponseEntity.ok(lossPage.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/lotId/{id}")
    @ApiOperation(value = "Performs a paginated search of loss based on the provided lot ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of loss that match the provided lot ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<LossDTO>> getLossByLotId(
            @ApiParam(value = "Lot ID", required = true)
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of loss to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Loss> lossPage = lossService.getLossByLotid(id, pageable);
        return ResponseEntity.ok(lossPage.stream().map(LossDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Return loss by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of loss by ID"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No loss found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<LossDTO> get(
            @ApiParam(value = "Loss ID", required = true)
            @PathVariable("id") Long id
    ) {
        Loss lossResponse = lossService.getLossById(id);
        LossDTO lossDTO = LossDTO.create(lossResponse);
        return ResponseEntity.ok(lossDTO);
    }

    @PostMapping
    @ApiOperation("Creates a new loss")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Loss created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<LossDTO> post(
            @ApiParam(value = "The details of the loss to create", required = true)
            @RequestBody @Valid LossDTO lossDTO) {
        Loss loss = converter(lossDTO, employeeService, lotService);
        Loss lossResponse = lossService.saveLoss(loss);
        return new ResponseEntity(LossDTO.create(lossResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an loss")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loss updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<LossDTO> update(
            @ApiParam(value = "Lot ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the loss to update", required = true)
            @RequestBody @Valid LossDTO lossDTO) {
        Loss loss = converter(lossDTO, employeeService, lotService);
        loss.setId(id);
        Loss lossResponse = lossService.updateLoss(loss);
        return new ResponseEntity(LossDTO.create(lossResponse), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an Loss by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loss deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Loss ID", required = true)
            @PathVariable("id") Long id) {
        lossService.deleteLoss(id);
        return ResponseEntity.ok("Registro de perdas excluída com sucesso!");
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

    public static Loss converter(LossDTO lossDTO, EmployeeService employeeService, LotService lotService) {
        ModelMapper modelMapper = new ModelMapper();
        Loss loss = modelMapper.map(lossDTO, Loss.class);

        loss.setRegisterDate(LocalDate.parse(lossDTO.getRegisterDate()));

        if (lossDTO.getLotId() != null) {
            Optional<Lot> lot = lotService.getLotById(lossDTO.getLotId());
            loss.setLot(lot.get());
        }

        if (lossDTO.getEmployeeId() != null) {
            Optional<Employee> employee = employeeService.getEmployeeById(lossDTO.getEmployeeId());
            loss.setEmployee(employee.get());
        }
        return loss;
    }

}