package com.vbatecan.portfolio_manager.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.List;
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

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "certificate_id", referencedColumnName = "id")
	@Nullable
	private List<CertificatesUpload> certificatesUploads;

	@NotNull
	@Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "Title should be less than 255 characters.")
	private String title;

	@NotNull
	@Column(name = "issuer", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "issuer should be less than 255 characters.")
	private String issuer;

	@NotNull
	@Column(name = "credential_id", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "Credential ID should be less than 255 characters.")
	private String credentialId;

	@NotNull
	@Column(name = "credential_url", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "Credential URL should be less than 255 characters.")
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