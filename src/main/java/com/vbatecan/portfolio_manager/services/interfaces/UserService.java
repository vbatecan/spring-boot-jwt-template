package com.vbatecan.portfolio_manager.services.interfaces;

import com.vbatecan.portfolio_manager.models.entities.User;

import java.util.Optional;

public interface UserService {
	Optional<User> findByUsername(String username);
}
