package com.vbatecan.springboot_jwt.services.interfaces;

import com.vbatecan.springboot_jwt.models.entities.User;

import java.util.Optional;

public interface UserService {
	Optional<User> findByUsername(String username);
}
