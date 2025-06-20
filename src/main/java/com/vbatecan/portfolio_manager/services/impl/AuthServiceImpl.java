package com.vbatecan.portfolio_manager.services.impl;

import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.output.LoginSuccessfulResponse;
import com.vbatecan.portfolio_manager.securities.JwtService;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import com.vbatecan.portfolio_manager.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

	private final JwtService jwtService;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Login a user with a given username and password.
	 *
	 * @param username the username
	 * @param password the password
	 * @return an optional user if the credentials match, or an empty optional if they don't
	 */
	@Override
	public Optional<LoginSuccessfulResponse> login(@NonNull String username, @NonNull String password) throws UsernameNotFoundException {
		Optional<User> userOptional = userService.findByUsername(username);

		if ( userOptional.isEmpty() ) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		User user = userOptional.get();

		if ( passwordEncoder.matches(password, user.getPassword()) ) {
			String token = jwtService.generateToken(user);
			Long expiration = jwtService.getTokenExpirationTime(token);
			return Optional.of(
				new LoginSuccessfulResponse(token, user.toDTO(), expiration)
			);
		}

		return Optional.empty();
	}
}
