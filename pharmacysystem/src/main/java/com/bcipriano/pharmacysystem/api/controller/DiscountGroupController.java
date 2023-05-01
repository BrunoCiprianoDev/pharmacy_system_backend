package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.DiscountGroupDTO;
import com.bcipriano.pharmacysystem.api.dto.EmployeeDTO;
import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import com.bcipriano.pharmacysystem.model.entity.DiscountGroup;
import com.bcipriano.pharmacysystem.service.DiscountGroupService;
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
@RequestMapping(value = "/api/v1/discounts", produces = {"application/json"})
@RequiredArgsConstructor
public class DiscountGroupController {

    private final DiscountGroupService discountGroupService;

    @GetMapping
    @ApiOperation("Returns all discount groups registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of discount group"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<DiscountGroupDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of discount groups to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroup(pageable);
        return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of discount groups based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of discount groups that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<DiscountGroupDTO>> get(
            @ApiParam(value = "The query to search for discount group")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of discount groups to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroupByQuery(query, pageable);
        return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/startDate")
    @ApiOperation(value = "Returns discount groups by start date ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of discount groups that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<DiscountGroupDTO>> getByStartDate(
            @ApiParam(value = "Start date (YYYY_MM_DD)")
            @RequestParam("startDate") String startDate,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of discount groups to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroupByStartDate(startDate, pageable);
        return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/finalDate")
    @ApiOperation(value = "Returns discount groups by final date ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of discount groups that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<DiscountGroupDTO>> getByFinalDate(
            @ApiParam(value = "Final date (YYYY_MM_DD)")
            @RequestParam("startDate") String finalDate,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of discount groups to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<DiscountGroup> discountGroupPage = discountGroupService.getDiscountGroupByFinalDate(finalDate, pageable);
        return ResponseEntity.ok(discountGroupPage.stream().map(DiscountGroupDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Returns discount group by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of discount group by ID."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No discount group found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<DiscountGroupDTO> get(@PathVariable("id") Long id) {
        Optional<DiscountGroup> discountGroupResponse = discountGroupService.getDiscountGroupById(id);
        return ResponseEntity.ok(DiscountGroupDTO.create(discountGroupResponse.get()));
    }

    @PostMapping
    @ApiOperation("Creates a new discount group")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Discount group created successfully.", response = DiscountGroup.class),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<DiscountGroupDTO> post(
            @ApiParam(value = "The details of the discount group to create", required = true)
            @RequestBody @Valid DiscountGroupDTO discountGroupDTO) {
        DiscountGroup discountGroup = converter(discountGroupDTO);
        DiscountGroup discountGroupResponse = discountGroupService.saveDiscountGroup(discountGroup);
        return new ResponseEntity(DiscountGroupDTO.create(discountGroupResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an discount group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Discount group updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<DiscountGroupDTO> put(
            @PathVariable("id") Long id,
            @RequestBody @Valid DiscountGroupDTO discountGroupDTO) {
        DiscountGroup discountGroup = converter(discountGroupDTO);
        discountGroup.setId(id);
        DiscountGroup discountGroupResponse = discountGroupService.updateDiscountGroup(discountGroup);
        return new ResponseEntity(DiscountGroupDTO.create(discountGroupResponse), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an discount group by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Discount group deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        discountGroupService.deleteDiscountGroup(id);
        return ResponseEntity.ok("Grupo de desconto excluído com sucesso!");
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

    public static DiscountGroup converter(DiscountGroupDTO discountGroupDTO) {

        ModelMapper modelMapper = new ModelMapper();
        DiscountGroup discountGroup = modelMapper.map(discountGroupDTO, DiscountGroup.class);

        discountGroup.setStartDate(LocalDate.parse(discountGroupDTO.getStartDate()));
        discountGroup.setFinalDate(LocalDate.parse(discountGroupDTO.getFinalDate()));

        return discountGroup;

    }

}
