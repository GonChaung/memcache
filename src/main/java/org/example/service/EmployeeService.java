package org.example.service;

import org.example.dto.employee.EmployeeCreateDto;
import org.example.dto.employee.EmployeeResponseDto;
import org.example.dto.employee.EmployeeUpdateDto;
import org.example.dto.employee.TopPaidEmployeeDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto createEmployee(EmployeeCreateDto employeeCreateDto);
    EmployeeResponseDto getEmployeeById(Integer id);
    EmployeeResponseDto updateEmployee(Integer id, EmployeeUpdateDto employeeUpdateDto);
    void deleteEmployee(Integer id);
    List<TopPaidEmployeeDto> getTop10HighestPaidEmployees();
    String resignEmployee(Integer empNo,  LocalDate resignDate);
}
