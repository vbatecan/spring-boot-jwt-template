package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.entities.Education;
import com.vbatecan.portfolio_manager.models.input.EducationInput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EducationInputMapper {
    Education toEntity(EducationInput educationInput);
    void updateEntityFromInput(EducationInput educationInput, @MappingTarget Education education);
}
