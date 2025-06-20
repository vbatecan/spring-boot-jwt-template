package com.vbatecan.portfolio_manager.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@Table(name = "projects")
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	@Nullable
	private List<ProjectsUpload> uploads;

	@NotNull
	@Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "Title should be less than 255 characters.")
	private String title;

	@Column(name = "description", length = Integer.MAX_VALUE)
	@Max(value = 10000, message = "Title should be less than 10000 characters.")
	private String description;

	@NotNull
	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	@PastOrPresent
	private OffsetDateTime createdAt;

	@NotNull
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	@PastOrPresent
	private OffsetDateTime updatedAt;

}