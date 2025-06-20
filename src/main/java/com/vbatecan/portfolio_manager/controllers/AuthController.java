package com.vbatecan.portfolio_manager.controllers;

import com.vbatecan.portfolio_manager.models.input.AuthInput;
import com.vbatecan.portfolio_manager.models.output.LoginSuccessfulResponse;
import com.vbatecan.portfolio_manager.models.output.MessageResponse;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthInput login, HttpServletResponse response) {
		Optional<LoginSuccessfulResponse> successfulResponse = authService.login(login.username(), login.password());
		if ( successfulResponse.isPresent() ) {
			LoginSuccessfulResponse loginSuccessfulResponse = successfulResponse.get();
			Cookie cookie = new Cookie("token", loginSuccessfulResponse.token());
			cookie.setPath("/");
			cookie.setMaxAge(3600);
			cookie.setDomain("localhost");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			return ResponseEntity.ok(loginSuccessfulResponse);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password.", false));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("token", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		cookie.setDomain("localhost");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return ResponseEntity.ok(new MessageResponse("Logged out.", true));
	}
}
