package com.opsnow.backend.employee.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmployeeAverageSalaryDto {
    private String locationName;

    private Long deptWithMostEmployees;

    private String deptEmployeeCount;

    private Long avgSalaryOfLowestDep;
}
