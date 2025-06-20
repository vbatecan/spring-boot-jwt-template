package com.vbatecan.portfolio_manager.models.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.Project}
 */
public record ProjectDTO( UUID id, UserDTO user, @NotNull String title, String description, List<ProjectsUploadDTO> uploads,
                          @NotNull OffsetDateTime createdAt,
                          @NotNull OffsetDateTime updatedAt ) implements Serializable {
}