package com.vbatecan.springboot_jwt.services.impl;

import com.vbatecan.springboot_jwt.models.entities.User;
import com.vbatecan.springboot_jwt.repositories.UserRepository;
import com.vbatecan.springboot_jwt.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
