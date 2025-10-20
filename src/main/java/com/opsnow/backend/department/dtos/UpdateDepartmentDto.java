package com.opsnow.backend.department.dtos;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDepartmentDto {
    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 50, message = "must has max 50 characters")
    private String departmentName;
}
