package com.vbatecan.portfolio_manager.models.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProjectInput(
	@Size(max = 255, message = "Title should be less than 255 characters.")
	@NotNull String title,
	@Size(max = 10000, message = "Description should be less than 10000 characters.")
	String description,
	List<ProjectsUploadInput> uploads
) {
}
