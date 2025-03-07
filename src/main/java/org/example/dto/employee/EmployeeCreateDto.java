package org.example.dto.employee;

import lombok.Data;

@Data
public class EmployeeCreateDto extends MasterEmployeeDto {
    private String departmentNo;
    private String title;
    private Integer salary;
}
