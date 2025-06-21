package com.vbatecan.portfolio_manager.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "experiences")
public class Experience {
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
	@Size(max = 255, message = "Title should be less than 255 characters.")
	private String title;

	@Column(name = "position", length = Integer.MAX_VALUE)
	@Size(max = 255, message = "Position should be less than 255 characters.")
	private String position;

	@Column(name = "company", length = Integer.MAX_VALUE)
	@Size(max = 255, message = "Company name should be less than 255 characters.")
	private String company;

	@Column(name = "start_date")
	@PastOrPresent
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "description", length = Integer.MAX_VALUE)
	@Size(max = 10000, message = "Description should be less than 10000 characters.")
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


	@PrePersist
	public void prePersist() {
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}

}