package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.EducationDTO;
import com.vbatecan.portfolio_manager.models.entities.Education;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EducationMapper {
    EducationDTO toDTO(Education education);
}
