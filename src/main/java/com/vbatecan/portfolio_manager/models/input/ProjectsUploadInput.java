package com.vbatecan.portfolio_manager.models.input;

import jakarta.validation.constraints.NotNull;

public record ProjectsUploadInput (
	@NotNull String url
){
}
