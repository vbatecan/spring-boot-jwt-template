package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.EducationDTO;
import com.vbatecan.portfolio_manager.models.entities.Education;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducationMapper {
    EducationDTO toDto(Education education);
}
