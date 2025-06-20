package com.vbatecan.portfolio_manager.models.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthInput(

	@NotNull(message = "Username is required")
	@Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
	String username,

	@NotNull(message = "Password is required")
	@Size(min = 8, max = 100, message = "Password must be between 8 and 32 characters")
	String password
) {


}
