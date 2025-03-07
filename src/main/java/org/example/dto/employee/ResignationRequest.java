package org.example.dto.employee;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResignationRequest {
    private LocalDate resignDate;
}
