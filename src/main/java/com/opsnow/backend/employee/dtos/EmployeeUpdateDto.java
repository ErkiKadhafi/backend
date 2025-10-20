package com.opsnow.backend.employee.dtos;

import com.opsnow.backend.validation.Uppercase;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeUpdateDto {
    @NotBlank
    @Size(min = 1, message = "must has min 1 character")
    @Size(max = 2, message = "must has max 2 characters")
    @Uppercase
    private String departmentCode;

    @NotNull
    @Pattern(regexp = "^[A-Z]\\d{1,2}$")
    private String locationCode;

    @NotNull
    @Digits(integer = 3, fraction = 0, message = "musr be in this format (ex: A1, B10)")
    private Integer tierCode;

    // private SupervisorDto supervisor;

    private Integer supervisorCode;

    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 100, message = "must has max 100 characters")
    private String empName;

    @NotNull
    private Integer salary;
}
