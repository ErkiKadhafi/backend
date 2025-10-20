package com.opsnow.backend.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmployeeSalaryGapDto {
    private String locationName;

    private String deptName;

    private String empName;

    private String tierName;

    private Long salary;

    private Long rank;
    
    private Long salaryGap;
}
