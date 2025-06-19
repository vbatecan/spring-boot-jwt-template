package com.vbatecan.portfolio_manager.models.entities;

import com.vbatecan.portfolio_manager.models.dto.UserDTO;
import com.vbatecan.portfolio_manager.models.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.GUEST;

	@Column(name = "created_at")
	private Instant createdAt;

	@Column(name = "updated_at")
	private Instant updatedAt;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new GrantedAuthority[]{new SimpleGrantedAuthority(role.name())});
	}

	public UserDTO toDTO() {
		return new UserDTO(
			id,
			username,
			role,
			createdAt,
			updatedAt
		);
	}
}