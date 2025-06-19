package com.vbatecan.portfolio_manager.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "certificates")
public class Certificate {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
	private String title;

	@NotNull
	@Column(name = "issuer", nullable = false, length = Integer.MAX_VALUE)
	private String issuer;

	@NotNull
	@Column(name = "credential_id", nullable = false, length = Integer.MAX_VALUE)
	private String credentialId;

	@NotNull
	@Column(name = "credential_url", nullable = false, length = Integer.MAX_VALUE)
	private String credentialUrl;

	@NotNull
	@Column(name = "image", nullable = false, length = Integer.MAX_VALUE)
	private String image;

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;

	@NotNull
	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@NotNull
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

}