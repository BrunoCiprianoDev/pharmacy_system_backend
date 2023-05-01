package com.bcipriano.pharmacysystem.api.controller;

import com.bcipriano.pharmacysystem.api.dto.EmployeeDTO;
import com.bcipriano.pharmacysystem.model.entity.Address;
import com.bcipriano.pharmacysystem.model.entity.Employee;
import com.bcipriano.pharmacysystem.service.EmployeeService;

import io.swagger.annotations.*;

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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/employees", produces = {"application/json"})
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @ApiOperation("Returns all employees registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of employees"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<EmployeeDTO>> get(
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of employees to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Employee> employeePage = employeeService.getEmployee(pageable);
        return ResponseEntity.ok(employeePage.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Performs a paginated search of employees based on the provided query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of employees that match the provided query"),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<EmployeeDTO>> get(
            @ApiParam(value = "The query to search for employees")
            @RequestParam("query") String query,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of employees to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Employee> employeePage = employeeService.getEmployeeByQuery(query, pageable);
        return ResponseEntity.ok(employeePage.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/position")
    @ApiOperation(value = "Returns employees by their job position.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of employees by position."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No employee was found with the provided position parameter"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<List<EmployeeDTO>> getByPosition(
            @ApiParam(value = "Position value (Ex: SELLER OR MANAGER)")
            @RequestParam("position") String position,
            @ApiParam(value = "The page number to be returned (starting at 0)", defaultValue = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "The maximum number of employees to be returned in a page", defaultValue = "10")
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @ApiParam(value = "The sorting direction (ASC or DESC)", defaultValue = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        Page<Employee> employeePage = employeeService.getEmployeeByPosition(position, pageable);
        return ResponseEntity.ok(employeePage.stream().map(EmployeeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Returns employees by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a paginated list of employees by ID."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 404, message = "No employee found with the provided ID."),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<EmployeeDTO> get(
            @ApiParam(value = "Employee ID")
            @PathVariable("id") Long id
    ) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(EmployeeDTO.create(employee.get()));
    }

    @PostMapping
    @ApiOperation("Creates a new employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee created successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<EmployeeDTO> post(
            @ApiParam(value = "The details of the employee to create", required = true)
            @RequestBody @Valid EmployeeDTO employeeDTO) {
        Employee employee = converter(employeeDTO);
        Employee employeeResponse = employeeService.saveEmployee(employee);
        return new ResponseEntity(EmployeeDTO.create(employeeResponse), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @ApiOperation("Update an employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee updated successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<EmployeeDTO> put(
            @ApiParam(value = "Employee ID", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "The details of the employee to update", required = true)
            @RequestBody @Valid EmployeeDTO employeeDTO) {
        Employee employee = converter(employeeDTO);
        employee.setId(id);
        Employee employeeResponse = employeeService.updateEmployee(employee);
        return new ResponseEntity(EmployeeDTO.create(employeeResponse), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee deleted successfully."),
            @ApiResponse(code = 400, message = "A validation error occurred while processing the request"),
            @ApiResponse(code = 500, message = "An internal server error occurred")
    })
    public ResponseEntity<?> delete(
            @ApiParam(value = "Employee ID", required = true)
            @PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity(HttpStatus.OK);
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

    public static Employee converter(EmployeeDTO employeeDTO) {

        ModelMapper modelMapper = new ModelMapper();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);

        employee.setBirthDate(LocalDate.parse(employeeDTO.getBirthDate()));

        Address address = modelMapper.map(employeeDTO, Address.class);
        employee.setAddress(address);

        return employee;

    }

}
