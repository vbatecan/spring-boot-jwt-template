package com.vbatecan.portfolio_manager.models.input;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectInput(
	@NotNull String title,
	String description,
	List<ProjectsUploadInput> uploads
) {
}
