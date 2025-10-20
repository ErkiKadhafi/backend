package com.opsnow.backend.department.dtos;

import com.opsnow.backend.validation.Uppercase;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    @NotBlank
    @Size(min = 1, message = "must has min 1 character")
    @Size(max = 2, message = "must has max 2 characters")
    @Uppercase
    private String departmentCode;

    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 50, message = "must has max 50 characters")
    private String departmentName;
}
