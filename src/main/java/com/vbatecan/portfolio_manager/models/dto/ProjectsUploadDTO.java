package com.vbatecan.portfolio_manager.models.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.ProjectsUpload}
 */
public record ProjectsUploadDTO( UUID id, @NotNull String url, @NotNull OffsetDateTime createdAt,
                                 @NotNull OffsetDateTime updatedAt ) implements Serializable {
}