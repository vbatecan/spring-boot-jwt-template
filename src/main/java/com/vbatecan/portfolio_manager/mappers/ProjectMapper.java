package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
	uses = {ProjectsUploadMapper.class, UserMapper.class})
public interface ProjectMapper {

	ProjectDTO toDTO(Project project);

	Project toEntity(ProjectDTO dto);
}
