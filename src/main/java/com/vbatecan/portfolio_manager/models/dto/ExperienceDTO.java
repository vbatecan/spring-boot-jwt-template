package com.vbatecan.portfolio_manager.models.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.Experience}
 */
public record ExperienceDTO( UUID id, @NotNull String title, String position, String company, LocalDate startDate,
                             LocalDate endDate, String description, @NotNull OffsetDateTime createdAt,
                             @NotNull OffsetDateTime updatedAt ) implements Serializable {
}