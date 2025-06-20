package com.vbatecan.portfolio_manager.services.interfaces;

import com.vbatecan.portfolio_manager.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
	Page<User> listAll(Pageable pageable);

	Optional<User> findByUsername(String username);
}
