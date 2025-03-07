package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.constant.Gender;
import org.example.model.converter.GenderConverter;


import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender", columnDefinition = "ENUM('M', 'F')")
    private Gender gender;

    @Column(name = "hire_date", columnDefinition = "DATE")
    private LocalDate hireDate;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @OrderBy("toDate DESC")
    private List<DeptEmp> deptEmpList;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @OrderBy("toDate DESC")
    private List<Title> titleList;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @OrderBy("toDate DESC")
    private List<Salary> salaryList;
}


