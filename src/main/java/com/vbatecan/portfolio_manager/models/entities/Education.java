package com.vbatecan.portfolio_manager.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "educations")
public class Education {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private UUID id;
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "Title should be less than 255 characters.")
	private String title;

	@NotNull
	@Column(name = "institution", nullable = false, length = Integer.MAX_VALUE)
	@Max(value = 255, message = "Institution should be less than 255 characters.")
	private String institution;

	@NotNull
	@Column(name = "start_date", nullable = false)
	@PastOrPresent
	private LocalDate startDate;

	@NotNull
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "description", length = Integer.MAX_VALUE)
	@Max(value = 10000, message = "Description should be less than 10000 characters.")
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