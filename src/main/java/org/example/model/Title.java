package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.composite.TitleId;

import java.time.LocalDate;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TitleId.class)
public class Title {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "title")
    private String title;

    @Id
    @Column(name = "from_date", columnDefinition = "DATE")
    private LocalDate fromDate;

    @Column(name = "to_date", columnDefinition = "DATE")
    private LocalDate toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    public Title(Employee employee, String title, LocalDate fromDate, LocalDate toDate) {
        this.empNo = employee.getEmpNo();
        this.employee = employee;
        this.title = title;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

}