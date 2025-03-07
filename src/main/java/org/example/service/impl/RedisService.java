package org.example.service.impl;


import org.example.dto.employee.TopPaidEmployeeDto;
import org.example.repository.EmployeeRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RedisService {

    private final EmployeeRepository employeeRepository;

    public RedisService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @CachePut(value = "employees", key = "'top-paid'")
    @Async // Runs asynchronously
    public CompletableFuture<List<TopPaidEmployeeDto>> evictAndRefreshCache() {
        System.out.println("Evicting and Refreshing Top Paid Employees Cache...");

        // Simulate fetching data from the repository
        List<Object[]> results = employeeRepository.findTop10HighestPaidEmployeesWithDepartment();

        // Process the results and map to DTOs
        List<TopPaidEmployeeDto> topPaidEmployees = results.stream().map(obj -> {
            Integer empNo = (Integer) obj[0];
            String firstName = (String) obj[1];
            String lastName = (String) obj[2];
            String deptName = (String) obj[3];
            Double maxSalary = ((Number) obj[4]).doubleValue();
            String title = getTitleForEmployee(empNo);
            return new TopPaidEmployeeDto(empNo, firstName, lastName, deptName, title, maxSalary);
        }).collect(Collectors.toList());

        // Return the result wrapped in CompletableFuture, cache will be updated by @CachePut
        return updateCache();
    }

    // This method is called to update the cache after eviction
    @CachePut(value = "employees", key = "'top-paid'")
    public CompletableFuture<List<TopPaidEmployeeDto>> updateCache() {
        // Simulating cache update, it returns the list in a CompletableFuture
        System.out.println("Evicting and Refreshing Top Paid Employees Cache...");

        // Simulate fetching data from the repository
        List<Object[]> results = employeeRepository.findTop10HighestPaidEmployeesWithDepartment();

        // Process the results and map to DTOs
        List<TopPaidEmployeeDto> topPaidEmployees = results.stream().map(obj -> {
            Integer empNo = (Integer) obj[0];
            String firstName = (String) obj[1];
            String lastName = (String) obj[2];
            String deptName = (String) obj[3];
            Double maxSalary = ((Number) obj[4]).doubleValue();
            String title = getTitleForEmployee(empNo);
            return new TopPaidEmployeeDto(empNo, firstName, lastName, deptName, title, maxSalary);
        }).collect(Collectors.toList());

        return CompletableFuture.completedFuture(topPaidEmployees);
    }

    public String getTitleForEmployee(Integer empNo) {
        return employeeRepository.findTitleByEmpNo(empNo);
    }

}