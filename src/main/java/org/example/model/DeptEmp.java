package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.composite.DeptEmpId;

import java.time.LocalDate;

@Entity
@Table(name = "dept_emp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DeptEmpId.class)
public class DeptEmp {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "dept_no", columnDefinition = "CHAR(4)") // Explicit column mapping
    private String deptNo;

    @Column(name = "from_date", columnDefinition = "DATE")
    private LocalDate fromDate; // FIXED

    @Column(name = "to_date", columnDefinition = "DATE")
    private LocalDate toDate; // FIXED

    @ManyToOne
    @JoinColumn(name = "emp_no", referencedColumnName = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "dept_no", referencedColumnName = "dept_no", insertable = false, updatable = false)
    private Department department;

    public DeptEmp(Employee employee, Department department, LocalDate fromDate, LocalDate toDate) {
        this.empNo = employee.getEmpNo();
        this.deptNo = department.getDeptNo();
        this.employee = employee;
        this.department = department;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
