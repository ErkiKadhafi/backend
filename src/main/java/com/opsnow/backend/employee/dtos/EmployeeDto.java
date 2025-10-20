package com.opsnow.backend.employee.dtos;

import java.time.LocalDateTime;

import com.opsnow.backend.department.dtos.DepartmentDto;
import com.opsnow.backend.location.dtos.LocationDto;
import com.opsnow.backend.tier.dtos.TierDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Integer empNo;

    private DepartmentDto department;

    private LocationDto location;

    private TierDto tier;

    private SupervisorDto supervisor;

    // private Integer supervisorCode;

    private String empName;

    private Integer salary;

    private LocalDateTime entryDate;
}
