package com.vbatecan.portfolio_manager.models.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EducationInput(
    @JsonProperty("title")
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title should be less than 255 characters.")
    String title,

    @JsonProperty("institution")
    @NotBlank(message = "Institution cannot be blank")
    @Size(max = 255, message = "Institution should be less than 255 characters.")
    String institution,

    @JsonProperty("start_date")
    @NotNull(message = "Start date cannot be null")
    @PastOrPresent
    LocalDate startDate,

    @JsonProperty("end_date")
    @NotNull(message = "End date cannot be null")
    LocalDate endDate,

    @JsonProperty("description")
    @Size(max = 10000, message = "Description should be less than 10000 characters.")
    String description
) {
}
