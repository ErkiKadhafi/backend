package com.opsnow.backend.location.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull
    @Pattern(regexp = "^[A-Z]\\d{1,2}$")
    private String locationCode;

    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 100, message = "must has max 100 characters")
    private String locationName;

    @NotBlank
    @Size(min = 3, message = "must has min 3 character")
    @Size(max = 150, message = "must has max 150 characters")
    private String locationAddress;
}
