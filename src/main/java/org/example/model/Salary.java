package org.example.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.composite.SalaryId;

import java.time.LocalDate;

@Entity
@Table(name = "salaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SalaryId.class)
public class Salary {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "from_date", columnDefinition = "DATE")
    private LocalDate fromDate;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "to_date", columnDefinition = "DATE")
    private LocalDate toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    public Salary(Employee employee, LocalDate fromDate, Integer salary, LocalDate toDate) {
        this.empNo = employee.getEmpNo();
        this.employee = employee;
        this.fromDate = fromDate;
        this.salary = salary;
        this.toDate = toDate;
    }
}