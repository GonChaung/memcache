package org.example.mapper;


import org.example.dto.employee.EmployeeResponseDto;
import org.example.dto.employee.MasterEmployeeDto;
import org.example.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "gender", expression = "java(org.example.model.constant.Gender.fromString(masterEmployeeDto.getGender()))")
    Employee toEntity(MasterEmployeeDto masterEmployeeDto);

    @Mapping(target = "gender", expression = "java(employee.getGender().toString())")
    @Mapping(target = "departmentNo", expression = "java(getLatestDepartment(employee))")
    @Mapping(target = "title", expression = "java(getLatestTitle(employee))")
    @Mapping(target = "salary", expression = "java(getLatestSalary(employee))")
    EmployeeResponseDto toDto(Employee employee);

    default String getLatestDepartment(Employee employee) {
        return employee.getDeptEmpList() != null && !employee.getDeptEmpList().isEmpty() ?
                employee.getDeptEmpList().get(0).getDeptNo() : null;
    }

    default String getLatestTitle(Employee employee) {
        return employee.getTitleList() != null && !employee.getTitleList().isEmpty() ?
                employee.getTitleList().get(0).getTitle() : null;
    }

    default Integer getLatestSalary(Employee employee) {
        return employee.getSalaryList() != null && !employee.getSalaryList().isEmpty() ?
                employee.getSalaryList().get(0).getSalary() : null;
    }
}

