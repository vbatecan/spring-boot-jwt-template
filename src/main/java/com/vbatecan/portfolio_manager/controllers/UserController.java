package com.vbatecan.portfolio_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.portfolio_manager.models.dto.UserDTO;
import com.vbatecan.portfolio_manager.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final ObjectMapper mapper;

	@GetMapping("")
	public ResponseEntity<PagedModel<?>> allUsers(@PageableDefault Pageable pageable) {
		return ResponseEntity.ok(new PagedModel<>(userService.listAll(pageable).map(user -> mapper.convertValue(user, UserDTO.class))));
	}
}
