package com.vbatecan.portfolio_manager.configs;

import com.vbatecan.portfolio_manager.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserService userService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		UserDetailsService detailsService = username -> userService.findByUsername(username).orElseThrow();
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(detailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
}
