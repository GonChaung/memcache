package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.composite.DeptManagerId;

import java.time.LocalDate;

@Entity
@Table(name = "dept_manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DeptManagerId.class)
public class DeptManager {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "dept_no", columnDefinition = "CHAR(4)")
    private String deptNo;

    @Column(name = "from_date", columnDefinition = "DATE")
    private LocalDate fromDate; // FIXED

    @Column(name = "to_date", columnDefinition = "DATE")
    private LocalDate toDate; // FIXED

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "dept_no", insertable = false, updatable = false)
    private Department department;
}
