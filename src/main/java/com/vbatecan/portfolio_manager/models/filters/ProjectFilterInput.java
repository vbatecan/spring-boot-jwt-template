package com.vbatecan.portfolio_manager.models.filters;

import org.springframework.lang.Nullable;

public record ProjectFilterInput(
	@Nullable

	String title,
	@Nullable
	String description
){
}
