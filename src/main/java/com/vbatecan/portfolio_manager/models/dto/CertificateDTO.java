package com.vbatecan.portfolio_manager.models.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.Certificate}
 */
public record CertificateDTO( UUID id, @NotNull String title, @NotNull String issuer, @NotNull String credentialId,
                              @NotNull String credentialUrl, @NotNull String image, String description,
                              @NotNull OffsetDateTime createdAt,
                              @NotNull OffsetDateTime updatedAt ) implements Serializable {
}