package org.example.service.impl;

import org.example.dto.employee.EmployeeCreateDto;
import org.example.dto.employee.EmployeeResponseDto;
import org.example.dto.employee.EmployeeUpdateDto;
import org.example.dto.employee.TopPaidEmployeeDto;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.EmployeeMapper;
import org.example.model.*;
import org.example.model.constant.Gender;
import org.example.repository.*;
import org.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final SalaryRepository salaryRepository;
    private final TitleRepository titleRepository;
    private final DeptEmpRepository deptEmpRepository;
    private final RedisService redisService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentRepository departmentRepository, SalaryRepository salaryRepository, TitleRepository titleRepository, DeptEmpRepository deptEmpRepository, RedisService redisService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
        this.salaryRepository = salaryRepository;
        this.titleRepository = titleRepository;
        this.deptEmpRepository = deptEmpRepository;
        this.redisService = redisService;
    }

    @Async // Runs this method asynchronously
    public void asyncTopPaidEmployees(){
        redisService.evictAndRefreshCache();
    }


    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        return List.of();
    }

    @Transactional
    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto employeeCreateDto) {
        Long maxEmpNo = employeeRepository.findMaxEmpNoWithLock();
        Integer nextEmpNo = maxEmpNo.intValue() + 1;
        Employee employee = employeeMapper.toEntity(employeeCreateDto);
        employee.setEmpNo(nextEmpNo);
        employee.setDeptEmpList(new ArrayList<>());
        employee.setTitleList(new ArrayList<>());
        employee.setSalaryList(new ArrayList<>());
        employee = employeeRepository.save(employee);

        if (employeeCreateDto.getDepartmentNo() != null) {
            Department department = departmentRepository.findById(employeeCreateDto.getDepartmentNo())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            DeptEmp deptEmp = new DeptEmp(employee, department, LocalDate.now(), LocalDate.of(9999, 1, 1));
            deptEmpRepository.save(deptEmp);

            employee.getDeptEmpList().add(deptEmp);
        }

        Employee employeeFind = employeeRepository.findById(nextEmpNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employeeCreateDto.getTitle() != null) {
            Title title = new Title(
                    employeeFind,
                    employeeCreateDto.getTitle(),
                    LocalDate.now(),
                    LocalDate.of(9999, 1, 1)
            );
            titleRepository.save(title);
            employee.getTitleList().add(title);
        }

        if (employeeCreateDto.getSalary() != null) {
            Salary salary = new Salary(
                    employeeFind,
                    LocalDate.now(),
                    employeeCreateDto.getSalary(),
                    LocalDate.of(9999, 1, 1)
            );
            salaryRepository.save(salary);
            employee.getSalaryList().add(salary);
        }
        EmployeeResponseDto responseDto = employeeMapper.toDto(employee);
        responseDto.setDepartmentNo(employeeCreateDto.getDepartmentNo());
        responseDto.setTitle(employeeCreateDto.getTitle());
        responseDto.setSalary(employeeCreateDto.getSalary());
        this.redisService.evictAndRefreshCache();
        return responseDto;
    }

    @Transactional
    @Override
//    @Cacheable(value = "employees", key = "#id")
    public EmployeeResponseDto getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Fetch collections separately
        employee.setDeptEmpList(employeeRepository.findByIdWithDept(id).orElse(employee).getDeptEmpList());
        employee.setSalaryList(employeeRepository.findByIdWithSalary(id).orElse(employee).getSalaryList());
        employee.setTitleList(employeeRepository.findByIdWithTitle(id).orElse(employee).getTitleList());

        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Override
    public EmployeeResponseDto updateEmployee(Integer empNo, EmployeeUpdateDto employeeUpdateDto) {
        Employee employee = employeeRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        updateEmployeeDetails(employee, employeeUpdateDto);

        if (employeeUpdateDto.getDepartmentNo() != null) {
            updateEmployeeDepartment(employee, employeeUpdateDto.getDepartmentNo());
        }
        if (employeeUpdateDto.getTitle() != null) {
            updateEmployeeTitle(employee, employeeUpdateDto.getTitle());
        }
        if (employeeUpdateDto.getSalary() != null) {
            updateEmployeeSalary(employee, employeeUpdateDto.getSalary());
        }
        employee = employeeRepository.save(employee);

        // Run cache refresh asynchronously
        this.asyncTopPaidEmployees();

        return new EmployeeResponseDto(employee);  // Should work now
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Gender parseGender(String genderValue) {
        return switch (genderValue.trim().toUpperCase()) {
            case "M", "MALE" -> Gender.MALE;
            case "F", "FEMALE" -> Gender.FEMALE;
            default -> throw new IllegalArgumentException("Invalid gender value: " + genderValue +
                    ". Please use M/F or MALE/FEMALE.");
        };
    }

    @Override
    public void deleteEmployee(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }
        this.asyncTopPaidEmployees();
        employeeRepository.deleteById(id);
    }

    @Override
//    @Cacheable(value = "employees", key = "'top-paid'")
    public List<TopPaidEmployeeDto> getTop10HighestPaidEmployees() {
        System.out.println("Employee Fetching.....");
        List<Object[]> results = employeeRepository.findTop10HighestPaidEmployeesWithDepartment();
        return results.stream().map(obj -> {
            Integer empNo = (Integer) obj[0];
            String firstName = (String) obj[1];
            String lastName = (String) obj[2];
            String deptName = (String) obj[3];
            Double maxSalary = ((Number) obj[4]).doubleValue();
            String title = getTitleForEmployee(empNo);
            return new TopPaidEmployeeDto(empNo, firstName, lastName, deptName, title, maxSalary);
        }).toList();
    }


    public String getTitleForEmployee(Integer empNo) {
        return employeeRepository.findTitleByEmpNo(empNo);
    }

    private void updateEmployeeDetails(Employee employee, EmployeeUpdateDto employeeUpdateDto) {
        if (employeeUpdateDto.getFirstName() != null) {
            employee.setFirstName(employeeUpdateDto.getFirstName());
        }
        if (employeeUpdateDto.getLastName() != null) {
            employee.setLastName(employeeUpdateDto.getLastName());
        }
    }

    @Transactional
    public void updateEmployeeDepartment(Employee employee, String newDeptNo) {
        Department newDepartment = departmentRepository.findById(newDeptNo)
                .orElseThrow(() -> new RuntimeException("Department record not found for employee"));
        deptEmpRepository.findByEmpNo(employee.getEmpNo()).ifPresent(existingDeptEmp -> {
            existingDeptEmp.setToDate(LocalDate.now());
            deptEmpRepository.save(existingDeptEmp);
        });

        DeptEmp newDeptEmp = new DeptEmp(employee, newDepartment, LocalDate.now(), LocalDate.of(9999, 1, 1));
        deptEmpRepository.save(newDeptEmp);
    }

    @Transactional
    public void updateEmployeeTitle(Employee employee, String newTitle) {
        Title title = employee.getTitleList().stream()
                .filter(t -> t.getEmpNo().equals(employee.getEmpNo()))
                .findFirst()
                .orElse(null);

        if (title == null) {
            title = new Title(
                    employee,
                    newTitle,
                    LocalDate.now(),
                    LocalDate.of(9999, 1, 1)
            );
            titleRepository.save(title);
            employee.getTitleList().add(title);
        } else {
            // If title exists, update it by creating a new record with a new fromDate
            // Create a new title entity to avoid changing the primary key
            Title newTitleEntity = new Title(
                    employee,
                    newTitle,
                    LocalDate.now(),
                    LocalDate.of(9999, 1, 1)
            );
            titleRepository.save(newTitleEntity);
            employee.getTitleList().add(newTitleEntity);
        }
    }


    private void updateEmployeeSalary(Employee employee, Integer newSalary) {
        Salary currentSalary = salaryRepository.findByEmpNoAndToDateIsNull(employee.getEmpNo())
                .orElse(null);
        if (currentSalary != null) {
            currentSalary.setToDate(LocalDate.now());
            salaryRepository.save(currentSalary);
        }

        Salary newSalaryEntity = new Salary(
                employee.getEmpNo(),
                LocalDate.now(),
                newSalary,
                LocalDate.of(9999, 1, 1),
                employee
        );
        salaryRepository.save(newSalaryEntity);
    }

    @Transactional
    public String resignEmployee(Integer empNo, LocalDate resignDate) {
        Employee employee = employeeRepository.findById(empNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<DeptEmp> deptEmps = deptEmpRepository.findByEmpNoAndToDateAfter(empNo, resignDate);
        for (DeptEmp deptEmp : deptEmps) {
            deptEmp.setToDate(resignDate);
            deptEmpRepository.save(deptEmp);
        }

        List<Title> titles = titleRepository.findByEmpNoAndToDateAfter(empNo, resignDate);
        for (Title title : titles) {
            title.setToDate(resignDate);
            titleRepository.save(title);
        }

        List<Salary> salaries = salaryRepository.findByEmpNoAndToDateAfter(empNo, resignDate);
        for (Salary salary : salaries) {
            salary.setToDate(resignDate);
            salaryRepository.save(salary);
        }
        return "Employee " + empNo + " has resigned successfully on " + resignDate;
    }

}
