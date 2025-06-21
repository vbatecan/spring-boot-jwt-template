package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProjectMapper.class, ProjectsUploadMapper.class})
public interface ProjectInputMapper {

	Project toEntity(ProjectInput projectInput);

	ProjectDTO toDTO(ProjectInput projectInput);
}
