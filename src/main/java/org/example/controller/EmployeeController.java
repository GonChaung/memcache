package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.employee.*;
import org.example.exception.ResourceNotFoundException;
import org.example.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);


    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody @Valid EmployeeCreateDto employeeCreateDto) {
        if (employeeCreateDto == null) {
            return ResponseEntity.badRequest().build(); // Return 400 if request body is invalid
        }

        EmployeeResponseDto employeeDto = employeeService.createEmployee(employeeCreateDto);
        URI location = UriComponentsBuilder
                .fromUriString("/employees/{id}")
                .buildAndExpand(employeeDto.getEmpNo())
                .toUri();
        return ResponseEntity.created(location).body(employeeDto); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        try {
            List<EmployeeResponseDto> employees = employeeService.getAllEmployees();
            if (employees.isEmpty()) {
                return ResponseEntity.noContent().build(); // Return 204 if no employees
            }
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return 500
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Integer id) {
        try {
            EmployeeResponseDto employeeDto = employeeService.getEmployeeById(id);
            System.out.println(employeeDto.getGender());
            return ResponseEntity.ok(employeeDto); // Return 200 if employee found
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if not found
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Integer id,
            @RequestBody @Valid EmployeeUpdateDto employeeUpdateDto) {
        if (employeeUpdateDto == null) {
            return ResponseEntity.badRequest().build(); // Return 400 if request body is invalid
        }
        try {
            EmployeeResponseDto updatedEmployee = employeeService.updateEmployee(id, employeeUpdateDto);
            return ResponseEntity.ok(updatedEmployee); // Return 200 if updated successfully
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee with ID " + id + " has been deleted."); // Return 200 with success message
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with ID " + id + " not found."); // Return 404 if not found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting employee."); // Return 500 for other errors
        }
    }

    @GetMapping("/top-paid")
    public ResponseEntity<List<TopPaidEmployeeDto>> getTop10HighestPaidEmployees() {
        List<TopPaidEmployeeDto> topPaidEmployees = employeeService.getTop10HighestPaidEmployees();
        return ResponseEntity.ok(topPaidEmployees);
    }

    @PostMapping("/{empNo}/resign")
    public ResponseEntity<String> resignEmployee(
            @PathVariable Integer empNo,
            @RequestBody(required = false) ResignationRequest resignationRequest) {

        LocalDate resignDate = resignationRequest != null && resignationRequest.getResignDate() != null
                ? resignationRequest.getResignDate()
                : LocalDate.now();

        String message = employeeService.resignEmployee(empNo, resignDate);
        return ResponseEntity.ok(message);
    }
}
