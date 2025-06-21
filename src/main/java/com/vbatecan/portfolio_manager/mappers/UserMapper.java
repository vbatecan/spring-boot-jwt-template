package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.UserDTO;
import com.vbatecan.portfolio_manager.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO toDTO(User user);
	@Mapping(target = "password", ignore = true)
	User toEntity(UserDTO userDTO);
}
