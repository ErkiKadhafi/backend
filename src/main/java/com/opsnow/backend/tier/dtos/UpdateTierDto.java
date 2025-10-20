package com.opsnow.backend.tier.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTierDto {
    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 100, message = "must has max 100 characters")
    private String tierName;
}
