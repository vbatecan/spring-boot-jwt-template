package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.entities.Certificate;
import com.vbatecan.portfolio_manager.models.input.CertificateInput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificateInputMapper {
    Certificate toEntity(CertificateInput certificateInput);
    void updateEntityFromInput(CertificateInput certificateInput, @MappingTarget Certificate certificate);
}
