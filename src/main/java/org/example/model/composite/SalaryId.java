package org.example.model.composite;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryId implements Serializable {
    private Integer empNo;
    private LocalDate fromDate;
}