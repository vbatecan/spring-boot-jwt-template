package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.ProjectsUploadDTO;
import com.vbatecan.portfolio_manager.models.entities.ProjectsUpload;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectsUploadMapper {

	ProjectsUploadDTO toDTO(ProjectsUpload upload);
	ProjectsUpload toEntity(ProjectsUploadDTO uploadDTO);
}
