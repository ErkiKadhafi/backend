package com.opsnow.backend.tier.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TierDto {
    @NotNull
    @Digits(integer = 3, fraction = 0, message = "musr be in this format (ex: A1, B10)")
    private Integer tierCode;

    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 100, message = "must has max 100 characters")
    private String tierName;
}
