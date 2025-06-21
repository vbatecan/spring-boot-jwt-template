package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.dto.ProjectsUploadDTO;
import com.vbatecan.portfolio_manager.models.dto.UserDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.entities.ProjectsUpload;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

	ProjectDTO toDTO(Project project);

	Project toEntity(ProjectDTO dto);

	UserDTO toDTO(User user);

	@Mapping(target = "password", ignore = true)
	User toEntity(UserDTO userDTO);

	ProjectsUploadDTO toDTO(ProjectsUpload upload);

	ProjectsUpload toEntity(ProjectsUploadDTO uploadDTO);

	Project toEntity(ProjectInput projectInput);

	ProjectDTO toDTO(ProjectInput projectInput);
}
