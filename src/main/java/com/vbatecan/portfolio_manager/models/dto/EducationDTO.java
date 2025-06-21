package com.vbatecan.portfolio_manager.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.Education}
 */
@Builder
public record EducationDTO(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("title")
    String title,

    @JsonProperty("institution")
    String institution,

    @JsonProperty("start_date")
    LocalDate startDate,

    @JsonProperty("end_date")
    LocalDate endDate,

    @JsonProperty("description")
    String description,

    @JsonProperty("created_at")
    OffsetDateTime createdAt,

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt
) {
}
