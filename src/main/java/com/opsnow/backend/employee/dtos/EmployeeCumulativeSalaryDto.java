package com.opsnow.backend.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmployeeCumulativeSalaryDto {
    private String departmentCode;

    private Long empNo;

    private String empName;

    private Long cumulativeSalary;
}
