package com.vbatecan.portfolio_manager.models.dto;

import com.vbatecan.portfolio_manager.models.enums.Role;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for {@link com.vbatecan.portfolio_manager.models.entities.User}
 */
public record UserDTO( UUID id, String username, Role role, Instant createdAt,
                       Instant updatedAt ) implements Serializable {
}