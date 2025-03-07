package org.example.dto.employee;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Employee;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {
    private Integer empNo;
    private String firstName;
    private String lastName;
    private String departmentNo;
    private String title;
    private Integer salary;

    public EmployeeUpdateDto(Employee employee) {
        this.empNo = employee.getEmpNo();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.departmentNo = getLatestDepartment(employee);
        this.title = getLatestTitle(employee);
        this.salary = getLatestSalary(employee);
    }

    private String getLatestDepartment(Employee employee) {
        return employee.getDeptEmpList() != null && !employee.getDeptEmpList().isEmpty() ?
                employee.getDeptEmpList().get(0).getDeptNo() : null;
    }

    private String getLatestTitle(Employee employee) {
        return employee.getTitleList() != null && !employee.getTitleList().isEmpty() ?
                employee.getTitleList().get(0).getTitle() : null;
    }

    private Integer getLatestSalary(Employee employee) {
        return employee.getSalaryList() != null && !employee.getSalaryList().isEmpty() ?
                employee.getSalaryList().get(0).getSalary() : null;
    }
}

