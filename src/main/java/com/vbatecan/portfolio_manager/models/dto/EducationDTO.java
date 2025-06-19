package com.vbatecan.portfolio_manager.models.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.Education}
 */
public record EducationDTO( UUID id, @NotNull String title, @NotNull String institution, @NotNull LocalDate startDate,
                            @NotNull LocalDate endDate, String description, @NotNull OffsetDateTime createdAt,
                            @NotNull OffsetDateTime updatedAt ) implements Serializable {
}