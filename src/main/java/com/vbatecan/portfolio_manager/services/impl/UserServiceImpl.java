package com.vbatecan.portfolio_manager.services.impl;

import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.repositories.UserRepository;
import com.vbatecan.portfolio_manager.services.interfaces.UserService;
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
