package org.example.dto.employee;

import lombok.Data;

import java.util.Date;

@Data
public class MasterEmployeeDto {
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;
}