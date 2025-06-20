package com.vbatecan.portfolio_manager.services.interfaces;

import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.output.LoginSuccessfulResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface AuthService {

	/**
	 * Login a user with a given username and password.
	 *
	 * @param username the username
	 * @param password the password
	 * @return an optional user if the credentials match, or an empty optional if they don't
	 */
	Optional<LoginSuccessfulResponse> login(@NonNull String username, @NonNull String password) throws UsernameNotFoundException;

	User getLoggedInUser();
}
