package org.example.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopPaidEmployeeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer empNo;
    private String firstName;
    private String lastName;
    private String deptName;
    private String title;
    private Double maxSalary;
}
