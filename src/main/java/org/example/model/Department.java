package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @Column(name = "dept_no", columnDefinition = "CHAR(4)") // Explicit column mapping // Match database type
    private String deptNo;

    @Column(name = "dept_name") // Explicit column mapping
    private String deptName;
}
